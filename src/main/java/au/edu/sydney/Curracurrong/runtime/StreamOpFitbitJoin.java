package au.edu.sydney.Curracurrong.runtime;

public class StreamOpFitbitJoin extends StreamOperator {

	
	public synchronized void execute() {
		// TODO Auto-generated method stub
		Boolean waitFlag = false;
		for(int i = 0; i < getNumInputChannels(); i++) {
            if (!isDataAvailable(i,0)) {
                waitFlag = true;
            }
        }
		if(!waitFlag) {
			for(int i = 0; i < getNumInputChannels(); i++) {
				System.out.println("Joining data from input " + i);
	            send(i, receive(0));
			}
		}
	}
}
