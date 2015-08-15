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

package au.edu.sydney.Curracurrong.runtime.server;

import java.io.IOException;
import java.util.Collections;
import java.util.Hashtable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import au.edu.sydney.Curracurrong.datatype.DRecord;
import au.edu.sydney.Curracurrong.datatype.DString;
import au.edu.sydney.Curracurrong.datatype.ParsingException;
import java.util.Enumeration;

import au.edu.sydney.Curracurrong.runtime.Communicator;
import au.edu.sydney.Curracurrong.runtime.StreamOperator;
import au.edu.sydney.Curracurrong.runtime.SystemStatus;
import au.edu.sydney.Curracurrong.scheduler.Scheduler;
import au.edu.sydney.Curracurrong.util.NetworkAddress;

/**
 * This class is responsible to receive messages from other nodes and manage them to put in proper
 * input queues of stream operators and also notify scheduler about it. In first step we assume that
 * the stream operators network is static and communicator knows the location of other stream operators
 * on other nodes.
 * 
 * Communicator is singleton.
 *
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public class ServerCommunicator extends Communicator {

    /**
     * Singleton instance of a Communicator
     */
    private static ServerCommunicator instance=null;

    /**
     * A table that maps channel_id to source node of the channel
     */
    private Hashtable channelSourceMap;

    /**
     * A table that maps channel_id to destination node of the channel
     */
    private Hashtable channelDestMap;

    /**
     * A table that maps source node to the channel_id
     */
    private Hashtable sourceChannelMap;

    /**
     * A table that maps destination node to the channel_id
     */
    private Hashtable destChannelMap;

    /**
     * A table that maps the operator to a sensor node where operator is deployed
     */
    //private Hashtable sensorNodeMap;

    /**
     * A table that maps the operator_id to the operator object
     */
    private Hashtable operatorRefMap;

    /**
     * Radiogram connection
     */
     DatagramSocket receiveSocket;

     /**
      * flag to check if the radio receiver has been stopped
      */
     private boolean isConnectionClosed = false;

     /**
      * port at which Administrator listens
      */
     private static final int HOST_PORT = 200;

     /**
      * Buffer that holds the data when radio is turned off
      */
     private Hashtable dataBuff;

    private ServerCommunicator() {
        channelSourceMap = new Hashtable();
        channelDestMap = new Hashtable();
        sourceChannelMap = new Hashtable();
        destChannelMap = new Hashtable();
        sensorNodeMap = new Hashtable();
        operatorRefMap = new Hashtable();
        dataBuff = new Hashtable();

        try{
            receiveSocket = new DatagramSocket(HOST_PORT);
            System.out.println("start listening on port 200");
        }catch(Exception e){
            System.out.println("CASCADE: failed during opening the connection in Communicator");
        }
    }

    /**
     * This method returns the singleton instance of the Communicator
     * @return communicator instance
     */
    public static ServerCommunicator getCommunicatorInstance() {
        if(instance == null) {
            instance = new ServerCommunicator();
        }
        return instance;
    }

    /**
     * This method adds the stream operator to the routing tables
     * @param so stream operator
     * @param senseNode sensor node where the operator is deployed
     */
    public synchronized void addStreamOperator(StreamOperator so, String senseNode){
        int numInputs = so.getNumInputChannels();
        int numOutputs = so.getNumOutputChannels();
        int opId = so.getOperatorID();
        for(int idx=0; idx < numInputs; idx++){
            int chId = so.getInputChannel(idx).getQueueID();
            int source = so.getInputChannel(idx).getSenderOperatorID();
            int dest = so.getInputChannel(idx).getReceiverOperatorID();
            if(!channelDestMap.containsKey(Integer.valueOf(chId))) {
                channelDestMap.put(Integer.valueOf(chId), Integer.valueOf(dest));
                destChannelMap.put(Integer.valueOf(dest), Integer.valueOf(chId));
                channelSourceMap.put(Integer.valueOf(chId), Integer.valueOf(source));
                sourceChannelMap.put(Integer.valueOf(source), Integer.valueOf(chId));
            }
        }
        for(int idx=0; idx < numOutputs; idx++) {
            int chId = so.getOutputChannel(idx).getQueueID();
            int source = so.getOutputChannel(idx).getSenderOperatorID();
            int dest = so.getOutputChannel(idx).getReceiverOperatorID();
            if(!channelSourceMap.containsKey(Integer.valueOf(chId))) {
                channelSourceMap.put(Integer.valueOf(chId), Integer.valueOf(source));
                sourceChannelMap.put(Integer.valueOf(source), Integer.valueOf(chId));
                channelDestMap.put(Integer.valueOf(chId), Integer.valueOf(dest));
                destChannelMap.put(Integer.valueOf(dest), Integer.valueOf(chId));
            }
        }
    
        /*sensorNodeMap.put(Integer.valueOf(opId), senseNode);
        System.out.println(" Sensor node to be placed in map " + senseNode);*/
        operatorRefMap.put(Integer.valueOf(opId), so);
    }

    /**
     * This method sends the data to the next operator on the channel.
     * <ul>
     * <li>If the operator is deployed on the same node then simply put the data on to the
     * input channel of the next operator.
     * <li>If the operator is not on the same node then based on the routing table
     * propagate data to the right node as radiogram
     * </ul>
     * @param channelId channel id on which data is to be sent
     * @param data data
     */
    public synchronized void send(int channelId, Object data){
        Integer src = null, dest = null;
        int chId = channelId;
        try {
            if(channelDestMap.containsKey(Integer.valueOf(chId))) {
                dest = Integer.valueOf(channelDestMap.get(Integer.valueOf(chId)).toString());
            }
            if(channelSourceMap.containsKey(Integer.valueOf(channelId))) {
                src = Integer.valueOf(channelSourceMap.get(Integer.valueOf(channelId)).toString());
            }
            if(sensorNodeMap == null ) {
                System.out.println("SenserNodeMap FOUND NULL ----->");
            } else {
                System.out.println("Src:: " + src + " Dest:: " + dest + " sensor for dest " + sensorNodeMap.size() + " " + sensorNodeMap.get(dest));
                if (sensorNodeMap.get(src)!= null && sensorNodeMap.get(dest) != null) {
                    String myAddress = null;
            		try {
            			myAddress = NetworkAddress.getAddress().getHostAddress();
            		} catch (Exception e) {
                        System.out.println("CASCADE: unable to fetch information about network interfaces");
            		}
            		
                    //assert (sensorNodeMap.get(src) == myAddress);
                    if (sensorNodeMap.get(src).equals(sensorNodeMap.get(dest))) {
                        StreamOperator so = (StreamOperator) operatorRefMap.get(dest);
                        for (int i = 0; i < so.getNumInputChannels(); i++) {
                            if (so.getInputChannel(i).getQueueID() == channelId) {
                                so.getInputChannel(i).push(data);
                                break;
                            }
                        }
                        fireStreamOperator(so);
                    } else {
                        propagateData(src.intValue(), dest.intValue(), data);
                    }
                } else {
                    System.out.println("Cascade:Data can't be propagated to next node...");
                }
            }
        }
        catch(Exception ex){
            System.out.println("CASCADE: failed at send from communicator" + ex);
            ex.printStackTrace();
        }
    }

    /**
     * This method sends the data received over radiogram connection
     * @param message data received from previous node
     */
    private void send(DRecord message) {
        try {
            Integer src = Integer.valueOf(message.getElement(0).tostring());
            Integer dest = Integer.valueOf(message.getElement(1).tostring());
            int dataLength = message.getSize() - 2;
            DRecord data =  new DRecord(dataLength);
            for(int i = 0; i < dataLength; i++ ) {
                data.setElement(i, message.getElement(i+2));
            }
            
            if(sensorNodeMap.get(src) != null && sensorNodeMap.get(dest) != null) {
                int chId1 = ((Integer) (sourceChannelMap.get(src))).intValue();
                int chId2 = ((Integer) (destChannelMap.get(dest))).intValue();

                StreamOperator so = (StreamOperator) operatorRefMap.get(dest);
                int queue=-1;
                for(int idx=0; idx<so.getNumInputChannels();idx++) {
                    if(so.getInputChannel(idx).getQueueID() == chId1) {
                        queue = idx;
                        break;
                    }
                }
                //so.getInputChannel(queue).push(data);
                so.getInputChannel(queue).push(message);
                fireStreamOperator(so);
            }
        }
        catch(ParsingException pe){
            System.out.println("CASCADE:failed in parsing while sending data");
        }
        catch(Exception e){
            System.out.println("CASCADE:failed during data propagation");
            e.printStackTrace();
        }
    }

    /**
     * This method creates the data to be sent to the next operator and notifies the scheduler
     * @param sender Sender operator
     * @param receiver Receiver operator
     * @param data data to be sent
     */
    void propagateData(int sender, int receiver, Object data) {
       int recLength = ((DRecord)data).getSize();       
       DRecord message = new DRecord(2+recLength);       
       try {
           message.setElement(0, new DString(String.valueOf(sender)));
           message.setElement(1, new DString(String.valueOf(receiver)));
           for(int i = 0; i < recLength; i++) {
               message.setElement(2+i, ((DRecord) data).getElement(i));
           }
       }       
       catch(ParsingException pe) {
           System.out.println("CASCADE: failed in parsing data during propagate");
       }
       dataBuff.put(Long.toString(System.currentTimeMillis()), message);

       try {
           if(!isConnectionClosed) {
        	   DatagramSocket socket = new DatagramSocket();
//        	   socket.setBroadcast(true);
               
               if(!dataBuff.isEmpty()) {
                   Enumeration keys = dataBuff.keys();
                   while(keys.hasMoreElements()) {
                       byte[] byteData;
                       ByteArrayOutputStream bout = new ByteArrayOutputStream();
                       ObjectOutputStream oos = new ObjectOutputStream(bout);
                       String key = (keys.nextElement()).toString();
                       DRecord tmpData = (DRecord)(dataBuff.get(key));
                       System.out.println("Data to be propagated from node-->" + tmpData.tostring());
                       try {
                            oos.writeObject(tmpData);
                       }
                       catch (IOException ioe) {
                           ioe.printStackTrace();
                       }
                       byteData = bout.toByteArray();
//                       DatagramPacket packet = new DatagramPacket(byteData, byteData.length, InetAddress.getByName("255.255.255.255"), HOST_PORT);
                       DatagramPacket packet = new DatagramPacket(byteData, byteData.length, InetAddress.getByName(sensorNodeMap.get(new Integer(receiver)).toString()), HOST_PORT);
                       socket.send(packet);
                       long prevSentPackets = SystemStatus.getSystemStatusInstance().getNumPacketsSent();
                       long prevSentBytes = SystemStatus.getSystemStatusInstance().getNumOfBytesSent();
                       SystemStatus.getSystemStatusInstance().setNumPacketSent(prevSentPackets + 1);
                       SystemStatus.getSystemStatusInstance().setnumOfBytesSent(prevSentBytes + tmpData.tostring().length());
                       dataBuff.remove(key);

                       byteData = null;
                       bout = null;
                       oos = null;
                       System.gc();
                   }
               } else {
                   System.out.println("Data Buffer is empty......Nothing to send");
               }
               socket.close();
               socket = null;
           } else {
               System.out.println("Connection is closed.. Data send pending...");
           }
       }
       catch(IOException ex) {
           System.out.println("CASCADE: data propagation failed in communicator");
       }
    }

    /**
     * This method is executed all the time and ready for receiving a messages
     * from other stream operators in other nodes or same node
     */
    public void run() {
    	System.out.println("Communicator in SErver");
        while (true) {
            try {
//            	System.out.println("Is connection closed [" + isConnectionClosed + "]");
                if(!isConnectionClosed){
//                	System.out.println("Is connection closed [" + isConnectionClosed + "]");
                    byte[] data = new byte[4096];

                    DatagramPacket packet = new DatagramPacket(data, data.length);
                    receiveSocket.receive(packet);
                	System.out.println("Received packet from [" + packet.getSocketAddress() + "]");
                    ByteArrayInputStream bin = new ByteArrayInputStream(data);
                    ObjectInputStream ois = new ObjectInputStream(bin);
                    DRecord receiveData = null;
                    try {
                        receiveData = (DRecord) ois.readObject();                        
                    }
                    catch (IOException ioe) {ioe.printStackTrace();}
                    catch (ClassNotFoundException cnfe) {cnfe.printStackTrace();}
//                    catch (IllegalAccessException iae) {iae.printStackTrace();}
//                    catch (InstantiationException ie) {ie.printStackTrace();}

                    // parse received string and extract sender and receiver stream operator ID
                    String myAddress = null;
            		try {
            			myAddress = NetworkAddress.getAddress().getHostAddress();
            		} catch (Exception e) {
                        System.out.println("CASCADE: unable to fetch information about network interfaces");
            		}
            		
            		 System.out.println("myAddress = [" + myAddress + "]");
                     System.out.println("receivedata  = [" + receiveData.tostring() + "]");
                     System.out.println("sensor map = [" + sensorNodeMap + "]");
            		
                    try {
                        if(sensorNodeMap.containsKey(Integer.valueOf(receiveData.getElement(1).tostring()))) {
                            String receiverAddress = (sensorNodeMap.get(Integer.valueOf(receiveData.getElement(1).tostring()))).toString();
                            if(receiverAddress.equals(myAddress)) {
                                //send data to next operator
                                send(receiveData);
                            }
                        }
                    }
                    catch(ParsingException pe) {
                        System.out.println("CASCADE: failed in parsing after receiving data");
                    }
                } else {
                    System.out.println("Communicator: connection is being closed");
                    closeConnection();
                }
            }
            catch(IOException ie) {
                System.out.println("Communicator: connection is being closed");
                closeConnection();
            }
            catch(Exception e){
                System.err.println("CASCADE: Caught " + e +  "in Communicator while reading received data");
                e.printStackTrace();
            }
        }
    }

    /**
     * This method fires the operator when data is available at input channel.
     * It adds the fired operator to the ready queue of scheduler for execution.
     * @param so stream operator
     */
    void fireStreamOperator(StreamOperator so){
        synchronized(this){
            if(!Scheduler.getSchedulerInstance().isReady(so)){
                // Scheduler.getSchedulerInstance().addReadyTask(so);
            	synchronized (so) {
            		so.notifyAll();
            	}
                notify();
            }
        }
    }

    /**
     * When administrator receives the command to kill an operator, this method
     * removes the operator from the routing table entries.
     * @param so stream operator
     */
    synchronized void removeOperator(StreamOperator so) {
        if(operatorRefMap.contains(so)) {
            operatorRefMap.remove(Integer.valueOf(so.getOperatorID()));
            String chId = destChannelMap.get(Integer.valueOf(so.getOperatorID())).toString();
            channelDestMap.remove(chId);
            channelSourceMap.remove(chId);
            sourceChannelMap.remove(Integer.valueOf(so.getOperatorID()));
            destChannelMap.remove(Integer.valueOf(so.getOperatorID()));
            sensorNodeMap.remove(Integer.valueOf(so.getOperatorID()));
        } else {
            System.out.println("CASCADE: No operator entry found in Communicator");
        }
    }

   /**
     * This method adds the operator and its associated node to the routing table
     * @param opId operator id
     * @param sensor node at which the operator is deployed
     */
    public void addOperatorToTable(String opId, String sensor) {
        try {
            sensorNodeMap.put(Integer.valueOf(opId), sensor);
            System.out.println("Sensor node to be placed in map " + sensor );
        }
        catch(Exception pe){
            System.out.println("CASCADE: failed to add operator to routing table");
        }
    }

    /**
     * This method returns the sensorNodetable
     * @return sensorNode table
     */
    public Hashtable getSensorNodeMap() {
        return sensorNodeMap;
    }

    /**
     * This method is used to reestablish the connection when radio is turned back ON
     */
    public synchronized void reestablishConnection() {
        try{
            System.out.println("Trying to turn on receiver in Communicator" + this.getName());
            if(receiveSocket == null ) {
                //receiveConn = (RadiogramConnection) Connector.open("radiogram://:"+HOST_PORT);
                wait();
            }
            isConnectionClosed = false;
        }
        catch(InterruptedException ie) {
            ie.printStackTrace();
        }
        catch(Exception ie) {
            ie.printStackTrace();
        }
    }

    /**
     * This method is used to close the connection when the radio is turned OFF
     */
    public synchronized void closeConnection() {
        try{
            System.out.println("Trying to turn off Receiver in Communicator" + this.getName());
            if(receiveSocket != null) {
                receiveSocket.close();
                receiveSocket = null;
            }

            System.out.println("Is radio connection ON??" + true);
            System.out.println("Communicator is going to wait at " + System.currentTimeMillis());
            wait();
            System.out.println("Communicator is coming out from wait at " + System.currentTimeMillis());
            reestablishConnection();
        }
        catch(Exception ie) {
            System.out.println("CASCADE: Problem in Communicator while closing connection");
        }
    }

    /**
     * This method is used to set the flag isConnectionClosed
     * @param isConnectionClosed true if the connection is closed, false otherwise
     */
	public void setConnectionClosed(boolean isConnectionClosed) {
		this.isConnectionClosed = isConnectionClosed;
		if (!isConnectionClosed && receiveSocket == null) {
			synchronized (this) {
				try {
					receiveSocket = new DatagramSocket(HOST_PORT);
					notify();
				} catch (Exception e) {
					System.out
							.println("CASCADE: Error in opening the connection in Communicator"
									+ e.getMessage());
					e.printStackTrace();
				}
			}
		} else if (isConnectionClosed) {
			if (receiveSocket != null) {
				receiveSocket.close();
				receiveSocket = null;
			}
		}
	}

    /**
     * This method is used to set the radiogram connection
     * @param conn connector
     */
    void setRadioConnection(DatagramSocket socket) {
        receiveSocket = socket;
    }

    /**
     * This method returns the radiogram connector
     * @return connector
     */
    DatagramSocket getRadioConnection() {
        return receiveSocket;
    }
}