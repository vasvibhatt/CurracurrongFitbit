package au.edu.sydney.Curracurrong.runtime;

import java.util.HashMap;
import java.util.Hashtable;

import au.edu.sydney.Curracurrong.datatype.DFloat;
import au.edu.sydney.Curracurrong.datatype.DRecord;
import au.edu.sydney.Curracurrong.datatype.DString;
import au.edu.sydney.Curracurrong.datatype.ParsingException;
import au.edu.sydney.Curracurrong.scheduler.Scheduler;


public class StreamOpFitbitAverage extends StreamOpFilter {
	
	private HashMap<Integer, Float> totalForUserMap = new HashMap<Integer, Float>();
	private int parameter;
	
	public void initialize(int opID, Hashtable propertyMap) {
        // call constructor of super-class to initialize
        // channels
        super.initialize(opID, propertyMap);
        try{
	        DRecord property = (DRecord) propertyMap.get("field");
	        parameter = Integer.parseInt(property.getElement(1).tostring());
	        System.out.println("Initialising FitbitAverage");
        } catch(ParsingException ex){
        	ex.printStackTrace();
        }
    }

	
	public synchronized void execute() {
		
		DRecord rec = null;
		Communicator comm = (Communicator) Registry.getInstance().get("Communicator");
		
		int noOfSensors = comm.getSensorList().size();
		DRecord outRec = new DRecord(noOfSensors);
		
		if(isDataAvailable(noOfSensors-1)) {
			for(int itr=0; itr < noOfSensors-1; itr++) {
				rec = (DRecord) peek(itr);
				try {
					int sender = Integer.parseInt((rec.getElement(0).tostring()));
					float stepsAvg = Float.parseFloat(rec.getElement(7).tostring());
					if(totalForUserMap.containsKey(sender)){
						float tempAvg = totalForUserMap.get(sender);
						tempAvg = (tempAvg + stepsAvg)/2;
						stepsAvg = tempAvg;
					}
					totalForUserMap.put(sender, stepsAvg);
					
				} catch(ParsingException ex) {
					ex.printStackTrace();
				}
			}
			for(int itr=0; itr < noOfSensors-1; itr++) {
				receive();
			}
		
			float totalAvgSum = 0.0f;
			int countSender = 0;
			for(int idx : totalForUserMap.keySet()) {
				totalAvgSum = totalAvgSum + totalForUserMap.get(idx);
				
				String senseAddress = comm.getSensorAddress(idx);
				try{
					System.out.println("Setting new out recod at position" + countSender + " with " + senseAddress);
					outRec.setElement(countSender++, new DString(senseAddress));
				}catch(ParsingException ex) {
					ex.printStackTrace();
				}
			}
			
			if(countSender != 0){
				float avgForAllSender;
				avgForAllSender = totalAvgSum/countSender;
				try{
					outRec.setElement(countSender, new DFloat(avgForAllSender));
				} catch(ParsingException ex) {
					ex.printStackTrace();
				}
				System.out.println("Record sent to Sink from FitbitAverage " + outRec.tostring() );
				send(outRec);
			}
		}
		
	}

}
