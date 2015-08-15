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

import au.edu.sydney.Curracurrong.datatype.DInteger32;
import au.edu.sydney.Curracurrong.datatype.DRecord;
import au.edu.sydney.Curracurrong.datatype.DString;
import au.edu.sydney.Curracurrong.datatype.Data;
import au.edu.sydney.Curracurrong.datatype.ParsingException;
import java.util.Hashtable;

//import runtime.node.NodeAdministrator;
//import runtime.node.NodeCommunicator;
import au.edu.sydney.Curracurrong.runtime.server.ServerAdministrator;
import au.edu.sydney.Curracurrong.runtime.server.ServerCommunicator;

/**
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public abstract class StreamOperator {

     /**
     * Unique Operator Id
     * TODO: operatorID should be integer!!
     */
    private int operatorID;

    /**
     * Input queues from which the operator receives data from preceding operators
     */
    private InputQueue[] inputs;

    /**
     * Output queues
     */
    private OutputQueue[] outputs;

    /*
     * Number of input queues
     */
    private int numInputQueues;

    /*
     * Number of output queues
     */
    private int numOutputQueues;

    /**
     * Property map
     */
    protected Hashtable propertyMap;

    /**
     * Monitor used for waking up the operator
     */
    protected Object monitor = new Object();
    
    /**
     * Set operator ID
     * @param opId
     */
    protected void setOperatorId(int opId){
        operatorID = opId;
    }

    /**
     * This method returns the unique id of the operator
     * @return Operator Id
     */
    public int getOperatorID() {
        return operatorID;
    }

    /**
     * This method returns the input queues
     * @return list of input queues
     */
    public InputQueue[] getInputChannels() {
        return inputs;
    }

    public OutputQueue[] getOutputChannels() {
        return outputs;
    }

    public InputQueue getInputChannel(int index) {
        return inputs[index];
    }

    public OutputQueue getOutputChannel(int index) {
        return outputs[index];
    }
    public int getNumInputChannels() {
        return numInputQueues;
    }

    public int getNumOutputChannels() {
        return numOutputQueues;
    }

    /**
     * This method checks for the availability of the specified data in input channel
     * @param channel input channel number
     * @param item index of the data item
     * @return true if data available, false otherwise
     */
    protected boolean isDataAvailable(int channelNo, int item) {
        return inputs[channelNo].peek(item) != null;
    }

    /**
     * This method returns the top element of the channel and removes it.
     * @param channel input channel id
     * @return data
     */
    protected Object receive(int channelNo){
        return inputs[channelNo].pop();
    }

   /**
     * This method returns the n-th element of the channel.
     * @param channel input channel id
     * @return data
     */
    protected Object peek(int channelNo,int item){
        //if(inputs[channelNo].size()>= item) {
            return inputs[channelNo].peek(item);
        //}
        //return null;
    }

    /**
     * This method sends the data to next operator with specified output channel
     * @param channel_id output channel id
     * @param item data
     */
    protected void send(int channelNo, Object item){
        //String receiverOperators[][] = ServerAdministrator.getAdministratorInstance().getOutputputQueues(operatorID);
    	String receiverOperators[][] = ((Administrator) (Registry.getInstance().get("Administrator"))).getOutputputQueues(operatorID);
        int queueID = Integer.parseInt(receiverOperators[channelNo][0]);
        //ServerCommunicator.getCommunicatorInstance().send(queueID, item);
        ((Communicator) (Registry.getInstance().get("Communicator"))).send(queueID, item);
    }

        /**
     * This is an abstract method implemented in individual operators to initialize parameters
     * @param operatorID
     */
    public void initialize(int opID, Hashtable propertyMap) {

      
       this.operatorID = opID;
       this.propertyMap = propertyMap;

//       String senderOperators[][] = ServerAdministrator.getAdministratorInstance().getInputQueues(opID);
//       String receiverOperators[][] = ServerAdministrator.getAdministratorInstance().getOutputputQueues(opID);
//
       String senderOperators[][] = ((Administrator) (Registry.getInstance().get("Administrator"))).getInputQueues(opID);
       String receiverOperators[][] = ((Administrator) (Registry.getInstance().get("Administrator"))).getOutputputQueues(opID);

       numInputQueues = senderOperators.length;
       numOutputQueues = receiverOperators.length;

       // create input queues
       inputs = new InputQueue[numInputQueues];
       int sender, receiver;
       String type;
       int queueID;
       for (int i = 0; i < numInputQueues; i++) {
           sender = Integer.parseInt(senderOperators[i][2]);
           receiver = getOperatorID();
           queueID = Integer.parseInt(senderOperators[i][0]);
           type = senderOperators[i][1];
           inputs[i] = new InputQueue(sender, receiver, queueID, type);
       }

       // create output queues
       outputs = new OutputQueue[numOutputQueues];
       for (int i = 0; i < numOutputQueues; i++) {
           receiver = Integer.parseInt(receiverOperators[i][2]);
           sender = getOperatorID();
           queueID = Integer.parseInt(receiverOperators[i][0]);
           type = receiverOperators[i][1];
           outputs[i] = new OutputQueue(receiver, sender, queueID);
       }
    }



    /**
     * This is an abstract method implemented in individual operators
     */
    abstract public void execute();

    Data getPropertyValue(String propertyKey) {
        Data value = null;
        // filter operator specific section
        if (propertyMap.containsKey(propertyKey)) {
            DRecord elem = (DRecord) propertyMap.get(propertyKey);
            try {
                DString readValue = (DString) elem.getElement(1);
                //value = new DInteger32 (Integer.parseInt(readValue.tostring()));
                value = readValue;
            } catch (ParsingException ex) {
                System.out.println(ex);
            }
        }
        return value;
    }
    
	/**
	 * Wait for the Communicator to wake it up when there is input. 
	 */
	public void block() {
		synchronized (this) {
			try {
				this.wait(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}