/**
 * Copyright 2014 University of Sydney, Australia.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package au.edu.sydney.Curracurrong.runtime;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;


import au.edu.sydney.Curracurrong.datatype.DRecord;
import au.edu.sydney.Curracurrong.datatype.DString;
import au.edu.sydney.Curracurrong.datatype.Data;

/**
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public class StreamOpThreshold extends StreamOpFilter {

	/**
	 * condition passed as a parameter to Threshold filter
	 */
    private String condition;
    /**
     * Mapping between parameter and threshold to check for
     * This data is extracted from the condition
     */
    private HashMap<Integer, Integer> mapDataThreshold;
    /**
     * Mapping between parameter and relational condition 
     * i.e. '>' , '<', '=='
     */
    private HashMap<Integer, Character> mapDataCondition;

    /**
     * Initializes the stream operator
     * @param opID operator id
     * @param propertyMap property list
     */
    public void initialize(int opID, Hashtable propertyMap) {
        // call constructor of super-class to initialize
        // channels
        super.initialize(opID, propertyMap);
        mapDataThreshold = new HashMap<Integer, Integer>();
        mapDataCondition = new HashMap<Integer, Character>();
        Data value = getPropertyValue("check");
        if(value != null) {
             condition = value.tostring();
             StringTokenizer token = new StringTokenizer(condition, "&");
             
             while(token.hasMoreTokens()) {
            	 String cond = token.nextToken().trim();
            	 StringTokenizer intoken = new StringTokenizer(cond, "<>==");
            	 if(intoken.countTokens() == 0 || intoken.countTokens() > 2){
            		 System.out.println("Cascade: Invalid condition for threshold");
            		 
            	 } else {
            		 String tempfield = intoken.nextToken().trim().substring(1);
            		 Integer fieldnum = Integer.parseInt(tempfield);
            		 Integer threshold = Integer.parseInt(intoken.nextToken().trim());
            		 mapDataThreshold.put(fieldnum, threshold);
            		 if(cond.contains("<")) {
            			 mapDataCondition.put(fieldnum, '<');            			 
            		 } else if(cond.contains(">")) {
            			 mapDataCondition.put(fieldnum, '>');            			 
            		 } else if(cond.contains("==")) {
            			 mapDataCondition.put(fieldnum, '=');
            		 }
            	 }
            	 
             }
        }
    }
	
	
	@Override
	public void execute() {
		if(isDataAvailable(0)) {
			DRecord rec = (DRecord) peek(0);
			DRecord outRec = null;
			boolean flag = false;
			HashMap<Integer, Data> sendField = new HashMap<Integer, Data>();
			for(int key : mapDataThreshold.keySet()) {
				int threshold = mapDataThreshold.get(key);
				
				try{
					String monitoredData = rec.getElement(key).tostring();
					StringTokenizer token = new StringTokenizer(monitoredData,".");
					if(token.countTokens() == 2){
						Double field = Double.parseDouble(monitoredData);
						Double newThreshold = new Double(threshold);
						if(mapDataCondition.get(key) == '<'){
							if (field < newThreshold) {
								sendField.put(key, rec.getElement(key));
								//sendField.add(rec.getElement(key));
								flag = true;
							}
						} else if(mapDataCondition.get(key) == '>'){
							if (field > newThreshold) {
								sendField.put(key, rec.getElement(key));
								//sendField.add(rec.getElement(key));
								flag = true;
							}
						} else {
							if (field == newThreshold) {
								sendField.put(key, rec.getElement(key));
								//sendField.add(rec.getElement(key));
								flag = true;
							}
						}
					} else {
						Long field = Long.parseLong(monitoredData);
						Long newThreshold = new Long(threshold);
						if(mapDataCondition.get(key) == '<'){
							if (field < newThreshold) {
								sendField.put(key, rec.getElement(key));
								//sendField.add(rec.getElement(key));
								flag = true;
							}
						} else if(mapDataCondition.get(key) == '>'){
							if (field > newThreshold) {
								sendField.put(key, rec.getElement(key));
								//sendField.add(rec.getElement(key));
								flag = true;
							} 
						}else {
							if (field == newThreshold) {
								sendField.put(key, rec.getElement(key));
								//sendField.add(rec.getElement(key));
								flag = true;
							}
						}
					}
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
			outRec = new DRecord(sendField.size());
			int idx = 0;			
			for(int key : sendField.keySet()) {
				try {
					System.out.println("Value of the field " + String.valueOf(key) + " is " +  sendField.get(key).tostring() + " and exceeded Threshold\n");
					DString dataString = new DString("Value of the field " + String.valueOf(key) + " is " +  sendField.get(key).tostring() + " and has exceeded the threshold! \n");
					outRec.setElement(idx, dataString);
					idx++;
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			//send(outRec);
			receive();
			if (outRec.getSize() > 0) {
				//send(outRec);
				send(rec);
			}
		}
		
	}

}
