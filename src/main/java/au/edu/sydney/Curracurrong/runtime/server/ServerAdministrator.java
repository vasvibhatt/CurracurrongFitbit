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

import au.edu.sydney.Curracurrong.runtime.Administrator;
import au.edu.sydney.Curracurrong.runtime.OperatorFactory;
import au.edu.sydney.Curracurrong.runtime.RadioTimer;
import au.edu.sydney.Curracurrong.runtime.StreamOperator;
import au.edu.sydney.Curracurrong.runtime.TimeTriggered;
import au.edu.sydney.Curracurrong.scheduler.*;
import au.edu.sydney.Curracurrong.util.NetworkAddress;
import au.edu.sydney.Curracurrong.datatype.*;
import java.io.IOException;
import java.util.*;
import au.edu.sydney.Curracurrong.queryLanguage.ControlCentre;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import au.edu.sydney.Curracurrong.queryLanguage.SuspendRadio;

/**
 * Responsible of creating and deleting stream operators on a node. Later extension: migrate stream
 * operator to another node.
 * (currently we assume that some stream operators will be created based on some information which have passed in table
 * format to administrator, administrator created these operators in constructor and other stream operators will be created
 * based on receiving commands from gateway)
 * Administrator is singleton.
 *
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public class ServerAdministrator extends Administrator {

    /**
     * singleton instance of Administrator
     */
    private static ServerAdministrator instance = null;

    /**
     * This table maps the operator_id to all receiver operators
     */
    private Hashtable operatorDestinationMap; 

    /**
     * This table maps the operator_id to all sender operators
     */
    private Hashtable operatorSourceMap; 

    /**
     * This table maps the operator_id to operator object
     */
    private Hashtable operatorList;

    /**
     * Radiogram connector
     */
    private DatagramSocket receiveSocket;

    /**
     * port at which Administrator listens
     */
    private static final int HOST_PORT = 110;

    /**     
     * flag to check if the radio receiver has been stopped
     */
     private boolean isConnectionClosed = false;     

    /**
     * This method returns the instance of the Administrator class
     * @return instance
     */
    public static ServerAdministrator getAdministratorInstance() {
        if(instance == null) {
            instance = new ServerAdministrator();
        }
        return instance;
    }

    /**
     * Constructor
     */
    private ServerAdministrator() {
        operatorSourceMap = new Hashtable();
        operatorDestinationMap = new Hashtable();
        operatorList = new Hashtable();
        try{
            receiveSocket = new DatagramSocket(HOST_PORT);
            System.out.println("start listening on port" +HOST_PORT);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * This method is executed all the time and ready for receiving a messages
     * from other stream operators in other nodes or same node
     */
    public void run() {
    	System.out.println("Administrator in Server!!!");
         while (true) {   
           
            boolean isSchedStart = false;
            try {
                if(!isConnectionClosed) {
                	byte[] data = new byte[4096];
                	DatagramPacket packet = new DatagramPacket(data, data.length);
                    receiveSocket.receive(packet);
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
                    System.out.println("received data ->" + "[ " + receiveData.tostring() + " ]");
                    try {
                        if(receiveData.getElement(0).tostring().equals("Commit")) {
                            if(isSchedStart == false) {
                                isSchedStart = true;
                                if(!Scheduler.getSchedulerInstance().isAlive()) {
                                    Scheduler.getSchedulerInstance().startSchedule();
                                }
                            }
                        } else if(receiveData.getElement(0).tostring().equalsIgnoreCase("Created:")) {
                            String sensor = receiveData.getElement(1).tostring();                            
                            ControlCentre.getInstance().notifyGateway(sensor);
                        } else if(receiveData.getElement(0).tostring().equalsIgnoreCase("FreeMem")) {
                            sendFreeMem();
                        } else if( receiveData.getElement(0).tostring().equalsIgnoreCase("Memory")) {
                            String sender = receiveData.getElement(1).tostring();
                            long freemem = ((DInteger64)receiveData.getElement(2)).getValue();
                            SuspendRadio.getInstance().addFreeMemory(sender, freemem);
                        } else if(receiveData.getElement(0).tostring().equalsIgnoreCase("setTimer")) {
                            System.out.println("Setting Timer" + receiveData.getElement(2).tostring());
                            RadioTimer timerInstance = RadioTimer.getInstance(((DInteger64)receiveData.getElement(1)).getValue(), ((DInteger64)receiveData.getElement(2)).getValue());
                            timerInstance.start();
                        } else {
                            DRecord record = receiveData;
                            receiveCommand(record);
                        }
                    }
                    catch(ParsingException pe) {
                        System.out.println("CASCADE: Data parsing failed at Administrator");
                    }
                } else {
                    System.out.println("Administrator: connection is being closed");
                    closeConnection();
                }
            }
            catch(IOException ie) {
                System.out.println("Administrator: connection is being closed");
                closeConnection();
            }
            catch (Exception ex) {
            	ex.printStackTrace();
                System.out.println("CASCADE: Exception caught in Administrator while receiving data");
            }
        }
    }


    /**
     * Get the operator Id and returns the stream operator object
     * @param operator ID
     * @return stream operator object
     */
    public StreamOperator getOperatorObject(int opID) {
        StreamOperator stnode = (StreamOperator) operatorList.get(Integer.valueOf(opID));
        return stnode;
    }

    /**
     * This method gets operatorID as a parameter and returns the record of operatorIDs that will
     * send message to this operator
     * @param operatorID of receiver operator
     * @return the record of sender operator IDs
     */
    public String[][] getInputQueues(int operatorID) {
        String[][] result;

        // retrieve from operatorSourceMap hashtable based on operatorID
        result = (String[][]) operatorSourceMap.get(Integer.valueOf(operatorID));

        return result;
    }

    /**
     * This method gets and operatorID as a parameter and returns the record of operatorIDs that will
     * receive message from this operator
     * @param operatorID ID of sender operator
     * @return the record of receiver operator IDs
     */
    public String[][] getOutputputQueues(int operatorID) {
        String[][] result;

        // retrieve from operatorSourceMap hashtable based on operatorID
        result = (String[][]) operatorDestinationMap.get(Integer.valueOf(operatorID));

        return result;
    }


    /**
     * This method parses the command and accordingly creates a new operator
     * @param command message received from server
     * @throws ParsingException
     */
    public void createStreamOperator(DRecord command) throws ParsingException {
        StreamOperator newOp = null;
        boolean isTimeTriggered = false;
        Hashtable propertyMap = new Hashtable();
        String senseNode = null;
        try {
             // put properties from property list to hash map
            DRecord plist = (DRecord) command.getElement(8);
            for(int i = 0; i < plist.getSize(); i++) {
                DRecord element = (DRecord) plist.getElement(i);
                propertyMap.put(element.getElement(0).tostring(), element);
            }

            DRecord scheduling = (DRecord) propertyMap.get("schedulingSemantic");
            if (scheduling == null) {
                throw new ParsingException("operator doesn't have scheduling semantic");
            }
            DString schedSemantic = new DString(scheduling.getElement(1).tostring()); // Data.createFromString(scheduling.getElement(2).tostring(), scheduling.getElement(1).tostring());

            String operatorClassName = command.getElement(7).tostring();
            int operatorID = Integer.parseInt(command.getElement(2).tostring());
            senseNode = command.getElement(1).tostring();

            if ( schedSemantic.tostring().equals("TimeTriggered") ) {
                // create scheduling semantic based on scheduling semantic type
                DRecord start = (DRecord) propertyMap.get("start");
                long startValue = Long.parseLong(start.getElement(1).tostring());
                DInteger64 starttime = new DInteger64(startValue); //    Data.createFromString(start.getElement(2).tostring(), start.getElement(1).tostring());
                long tmpStart = starttime.getValue() + System.currentTimeMillis();
                starttime = new DInteger64(tmpStart);
                DRecord rep = (DRecord) propertyMap.get("repeat");
                int repValue = Integer.parseInt(rep.getElement(1).tostring());
                DInteger32 repeat = new DInteger32(repValue); // Data.createFromString(rep.getElement(2).tostring(), rep.getElement(1).tostring());
                DRecord inter = (DRecord) propertyMap.get("interval");
                long interValue = Long.parseLong(inter.getElement(1).tostring());
                DInteger64 interval = new DInteger64(interValue); // Data.createFromString(inter.getElement(2).tostring(), inter.getElement(1).tostring());

                DRecord commdelay = (DRecord) propertyMap.get("delay");
                /*long setdelay = Long.parseLong(commdelay.getElement(1).tostring());
                TimeTriggered.setCommDelay(setdelay);*/

                // create stream operator based on stream operator type
                newOp = OperatorFactory.getStreamOperator(operatorClassName);
                newOp.initialize(operatorID, propertyMap);
                ((TimeTriggered) newOp).setStartTime(starttime.getValue());
                ((TimeTriggered) newOp).setRepeat(repeat.getValue());
                ((TimeTriggered) newOp).setInterval(interval.getValue());                
                isTimeTriggered = true;
            } else {
                // create stream operator based on stream operator type
                newOp = OperatorFactory.getStreamOperator(operatorClassName);
                newOp.initialize(operatorID, propertyMap);
            }
        }
        catch(ParsingException pe) {
        	pe.printStackTrace();
            System.out.println("CASCADE:create stream Operator failed");
        }
        catch(ClassCastException ce){
        	ce.printStackTrace();
            System.out.println("CASCADE:create stream Operator failed");
        }
        catch(Exception ex) {
        	ex.printStackTrace();
            System.out.println("CASCADE:create stream Operator failed");
        }

        // add new stream operator to operator list
        operatorList.put(Integer.valueOf(newOp.getOperatorID()), newOp);
       
      /*  try {
            RadiogramConnection gatewayConn = (RadiogramConnection) Connector.open("radiogram://broadcast:110");
            DRecord message = new DRecord(2);
            DString data = new DString("Created:");
            DString sensor = new DString(senseNode);
            message.setElement(0, data);
            message.setElement(1, sensor);

            System.out.println("Sending message to gateway" + data);
            Datagram dg = gatewayConn.newDatagram(gatewayConn.getMaximumLength());
            System.out.println("Data to be sent to base -->"+message.tostring());
            byte[] byteData;

            ByteArrayOutputStream bout = new ByteArrayOutputStream();

            ObjectOutputStream oos = new ObjectOutputStream(bout);

            try {
                oos.writeObject(message);
            }
            catch (IOException ioe) {ioe.printStackTrace();}

            byteData = bout.toByteArray();
            dg.write(byteData);
            gatewayConn.send(dg);
        }
        catch(IOException ex) {

        }*/
        ServerCommunicator.getCommunicatorInstance().addStreamOperator(newOp, senseNode);

        if(isTimeTriggered == true) {
            addStreamOperatorToSched((TimeTriggered)newOp);
        } else {
            addStreamOperatorToSched(newOp);
        }        
    }

    /**
     * This method receives the command from the server
     * @param command command received from server
     */
   public void receiveCommand(DRecord command) {
        DRecord command_params = command;
        String sub;

        try {
             sub = command_params.getElement(0).tostring();
             if (sub.equals("create")) {
                // Input queues
                int in = Integer.parseInt(command_params.getElement(3).tostring());
                String[][] inputs = new String[in][3];
                int j = 0;
                DRecord element;
                DRecord record = (DRecord) command_params.getElement(4);
                for (int i = 0; i < in; i++) {
                    element = (DRecord) record.getElement(i);
                    // Channel ID
                    inputs[j][0] = element.getElement(0).tostring();
                    // Type
                    inputs[j][1] = element.getElement(1).tostring();
                    // Sender ID
                    inputs[j][2] = element.getElement(2).tostring();
                    j++;
                }
                operatorSourceMap.put(Integer.valueOf(command_params.getElement(2).tostring()), inputs);

                // Output queues
                int out = Integer.parseInt(command_params.getElement(5).tostring());

                String[][] outputs = new String[out][3];
                j = 0;
                record = (DRecord) command_params.getElement(6);
                for (int i = 0; i < out; i++) {
                    element = (DRecord) record.getElement(i);
                    // Channel ID
                    outputs[j][0] = element.getElement(0).tostring();
                    // Type
                    outputs[j][1] = element.getElement(1).tostring();
                    // Receiver ID
                    outputs[j][2] = element.getElement(2).tostring();
                    j++;
                }
                operatorDestinationMap.put(Integer.valueOf(command_params.getElement(2).tostring()), outputs);

                // create stream operator based on parameters
                createStreamOperator(command);
             } else if (sub.equals("destroy")) {
                StreamOperator killOp;
                int operatorID = Integer.parseInt(command.getElement(2).tostring());
                killOp = getOperatorObject(operatorID);
                killStreamOperator(killOp);
            }
             else if(sub.equals("r")) {
                 DRecord tmpData = (DRecord) command.getElement(1);
                 ServerCommunicator.getCommunicatorInstance().addOperatorToTable(tmpData.getElement(0).tostring(), tmpData.getElement(1).tostring());
             }
        }
        catch (ParsingException pe) {
            // situation where the command doesn't have proper DRecord format
        }
    }

    /**
     * This method adds an event-based stream operator to scheduler
     * @param so stream operator
     */
    void addStreamOperatorToSched(StreamOperator so){
        Scheduler.getSchedulerInstance().addIdleTask(so);
    }

    /**
     * This method adds a time-based stream operator to scheduler
     * @param so stream operator
     */
    void addStreamOperatorToSched(TimeTriggered so){
        Scheduler.getSchedulerInstance().addTimeTriggeredTask(so);
    }

    /**
     * This method removes stream operator from scheduler
     * @param so stream operator
     */
    synchronized void killStreamOperator(StreamOperator so){
        if(operatorList.contains(so)) {
            Scheduler.getSchedulerInstance().removeOperator(so);
            ServerCommunicator.getCommunicatorInstance().removeOperator(so);
            operatorDestinationMap.remove(Integer.valueOf(so.getOperatorID()));
            operatorSourceMap.remove(Integer.valueOf(so.getOperatorID()));
            operatorList.remove(Integer.valueOf(so.getOperatorID()));
        }
    }

    /**
     * This method sends the free memory to gateway
     */
	private void sendFreeMem() {
		DRecord message = new DRecord(3);
        String myAddress = null;
		try {
			myAddress = NetworkAddress.getAddress().getHostAddress();
		} catch (Exception e) {
            System.out.println("CASCADE: unable to fetch information about network interfaces");
		}

		DString sender = new DString(myAddress);
		long freeMem = Runtime.getRuntime().freeMemory();
		try {
			message.setElement(0, new DString("Memory"));
			message.setElement(1, sender);
			message.setElement(2, new DInteger64(freeMem));
		} catch (ParsingException pe) {
			System.out
					.println("CASCADE: failed in parsing while sending free memory to gateway");
		}
		try {
			if (!isConnectionClosed) {
				DatagramSocket socket = new DatagramSocket();
				// socket.setBroadcast(true);
				socket = new DatagramSocket();

				byte[] byteData;
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(bout);
				try {
					oos.writeObject(message);
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
				byteData = bout.toByteArray();
				// note: this is a CNAME put into the /etc/hosts file for now
				for (InetAddress addr : InetAddress.getAllByName("curracurrong.cluster")) {
					DatagramPacket packet = new DatagramPacket(byteData, byteData.length, addr, HOST_PORT);
					socket.send(packet);
				}
				socket.close();
				socket = null;
			}
		} catch (IOException ex) {
			System.out
					.println("CASCADE: data propagation failed in communicator");
		}
	}

    /**
     * This method is used to set the timer for each radio turning ON/OFF
     * @param sleepTime Time for which radio will be turned off
     * @param commTime Time for which radio will be turned on for communication
     */
    public void setRadioTimer(long sleepTime, long commTime) {
		DRecord message = new DRecord(3);
		try {
			message.setElement(0, new DString("setTimer"));
			message.setElement(1, new DInteger64(sleepTime));
			message.setElement(2, new DInteger64(commTime));
		} catch (ParsingException pe) {
			System.out
					.println("CASCADE: failed in parsing timer message at gateway");
		}
		int sendCount = 0;

		while (sendCount < 2) {
			try {
				DatagramSocket socket = new DatagramSocket();
//				socket.setBroadcast(true);

				byte[] byteData;
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(bout);
				oos.writeObject(message);
				byteData = bout.toByteArray();

				System.out.println("Sending timer information to all sensor"
						+ message.tostring());
				// note: this is a CNAME put into the /etc/hosts file for now
				for (InetAddress addr : InetAddress.getAllByName("curracurrong.cluster")) {
					DatagramPacket packet = new DatagramPacket(byteData, byteData.length, addr, HOST_PORT);
					socket.send(packet);
				}
				socket.close();
				socket = null;
				Thread.sleep(5000);
			} catch (IOException ie) {
				System.out
						.println("CASCADE: failed while sending sleep/communication time to nodes");
			} catch (InterruptedException e) {
				System.out
						.println("CASCADE: thread interrupted while sending time information");
			}
			sendCount++;
		}
    }

    /**
     * This method is used to reestablish the connection when radio is turned back ON
     */
	public synchronized void reestablishConnection() {
		try {
			System.out.println("Trying to turn on receiver in Administrator");
			if (receiveSocket == null) {
				// receiveConn = (RadiogramConnection)
				// Connector.open("radiogram://:"+HOST_PORT);
				wait();
			}
			isConnectionClosed = false;
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		} catch (Exception ie) {
			ie.printStackTrace();
		}
	}

    /**
     * This method is used to close the connection when the radio is turned OFF
     */
    public synchronized void closeConnection() {
        try{
            System.out.println("Trying to turn off Receiver in Administrator" + this.getName());
            if(receiveSocket != null) {
                receiveSocket = null;
            }
            System.out.println("Administrator is going to wait at " + System.currentTimeMillis());
            wait();
            System.out.println("Administrator is coming out from wait at " + System.currentTimeMillis());
            reestablishConnection();
            }
            catch(Exception ie) {
                System.out.println("CASCADE: Problem in Administrator while closing connection");
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
							.println("CASCADE: Error in opening the connection in Administrator"
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