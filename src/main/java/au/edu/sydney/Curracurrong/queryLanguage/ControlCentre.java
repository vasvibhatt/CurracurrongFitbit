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

package au.edu.sydney.Curracurrong.queryLanguage;

import au.edu.sydney.Curracurrong.datatype.ParsingException;
import java.util.*;

import org.antlr.runtime.*;
import au.edu.sydney.Curracurrong.datatype.DInteger16;
import au.edu.sydney.Curracurrong.datatype.DInteger32;
import au.edu.sydney.Curracurrong.datatype.DInteger64;
import au.edu.sydney.Curracurrong.datatype.DRecord;
import au.edu.sydney.Curracurrong.datatype.DString;
import au.edu.sydney.Curracurrong.scheduler.Scheduler;
import au.edu.sydney.Curracurrong.util.NetworkAddress;
import au.edu.sydney.Curracurrong.runtime.RadioTimer;
import au.edu.sydney.Curracurrong.runtime.server.ServerAdministrator;

import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

/**
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public class ControlCentre {

   /**
    * This represents the instance reference of singleton class ControlCentre
    */
   protected static ControlCentre instance = null;

   /**
    * singleton access method
    * @return instance of class
    */
   public static ControlCentre getInstance() {
      if (instance == null) {
         instance = new ControlCentre();
      }
      return instance;
   }

   /**
    * A map for all stream graph
    */
   Map<String, StreamGraph> queries;
   /**
    * A map for the java data type to the cascade data model
    */
   Map<String, String> typeMap;

   /**
    * Table that maps all the nodes to true if they are created
    */
   private Map<String, Boolean> isnodeCreated;

   /**
    * Map allocator context query -> Allocation
    */
   private Map<String,Allocator> contextTable;

   private Map<String, Integer> sensorNumOperators;

   private Map<String, Integer> sensorNumOperatorsCreated;
   /**
    *  constructor
    */
   protected ControlCentre () {
      queries = new HashMap<String,StreamGraph>();

      typeMap = new HashMap<String, String>();
      typeMap.put("java.lang.String", "DString");
      typeMap.put("java.lang.Byte", "DInteger8");
      typeMap.put("java.lang.Short", "DInteger16");
      typeMap.put("java.lang.Integer", "DInteger32");
      typeMap.put("java.lang.Long", "DInteger64");
      typeMap.put("java.lang.Float", "DFloat");
      isnodeCreated = new HashMap<String, Boolean>();
      contextTable = new HashMap<String, Allocator>();
      sensorNumOperators = new HashMap<String, Integer>();
      sensorNumOperatorsCreated = new HashMap<String, Integer>();
   }

   /**
    * Create StreamGraph with nodeCtr nodes and edgeCtr edges
      @return Object of a new Stream Graph
    */
   StreamGraph createGraph() {
       return new StreamGraph();
   }

   /**
    * Execute a query in the system and send it to network
    * @param name query name
    * @param fileName file name
    */
	void execute(String name, String fileName) {
		boolean suspendRadio = true;
		String myAddress = null;
		try {
			myAddress = NetworkAddress.getAddress().getHostAddress();
		} catch (Exception e) {
			System.out
					.println("CASCADE: unable to fetch information about network interfaces");
		}
		if (queries.containsKey(name)) {
			System.out.println("CASCADE: execute " + name);
			StreamGraph graph = queries.get(name);
			DRecord command = null;
			// create DRecord command object from each node in the graph
			List<DRecord> commandList = new LinkedList<DRecord>();

			StreamNode[] nodes_order = graph.computeTopologicalOrder();
			int size = nodes_order.length;
			int count;
			StreamNode streamnode;
			size--;
			for (int i = 0; i <= size; i++) {
				count = size - i;
				streamnode = nodes_order[count];
				if (streamnode.getSensorNode().equals("0.0.0.0")) {
					streamnode.setSensorNode(myAddress);
				}
				try {
					command = createCommand(streamnode, graph);
					commandList.add(command);
				} catch (Exception e) {
					System.out
							.println("CASCADE: Exception in ControlCenter execute:"
									+ e.toString());
				}
			}

			if (!graph.getStatus()) {
				graph.setStatus(true);
				// send routing table to other nodes
				for (StreamNode node : graph.getNodes()) {
					DRecord routingTable = new DRecord(2);
					try {
						routingTable.setElement(0, new DString("r"));
						DRecord routElement = new DRecord(2);
						DString id = new DString(Integer.toString(node.getId()));
						DString nodeAddress = new DString(node.getSensorNode());
						routElement.setElement(0, id);
						routElement.setElement(1, nodeAddress);
						routingTable.setElement(1, routElement);
					} catch (ParsingException pe) {
						System.out
								.println("CASCADE: failed while creating routing table");
					}
					for (StreamNode sNode : graph.getNodes()) {
						String sensor = sNode.getSensorNode();
						if (!sensor.equals(myAddress)) {
							try {
								byte[] byteData = new byte[256];
								ByteArrayOutputStream bout = new ByteArrayOutputStream();
								ObjectOutputStream oos = new ObjectOutputStream(
										bout);
								oos.writeObject(routingTable);
								byteData = bout.toByteArray();
								DatagramSocket socket = new DatagramSocket();
								DatagramPacket packet = new DatagramPacket(
										byteData, byteData.length,
										InetAddress.getByName(sensor), 110);

								socket.send(packet);
								socket.close();
							} catch (IOException ex) {
								System.out
										.println("CASCADE: problem in radiogram connection");
								ex.printStackTrace();
							} catch (Exception ex) {
								System.out
										.println("CASCADE: problem in finding route due to "
												+ ex);
							}
						}
						try {
							Thread.currentThread().sleep(10);
						} catch (InterruptedException ie) {
							System.out.println("CASCADE: thread interrupted!");
						}
					}

					ServerAdministrator.getAdministratorInstance().receiveCommand(
							routingTable);

					try {
						Thread.currentThread().sleep(30);
					} catch (InterruptedException ie) {
						System.out.println("CASCADE: thread interrupted!");
					}
				}

				System.out
						.println("CASCADE:Sent routing table to all nodes...");

				try {
					Thread.currentThread().sleep(10);
				} catch (InterruptedException ie) {
					System.out
							.println("CASCADE: Inserted delay is interrupted");
				}

/*-------------------------------------------------------------------------------------------------------------*/
//				 // computing sleep time for sensor nodes
//				 if(suspendRadio) {
//				 SuspendRadio radioInstance = SuspendRadio.getInstance();
//				 radioInstance.getSensorFreeMem();
//				 //long waitCount = 0;
//				 while(!isnodeCreated.keySet().equals(SuspendRadio.getInstance().getFreeMemory().keySet())) {// || waitCount < 10000) {
//				
//				 }
//				 /*********radioInstance.computeSleepTime();
//				 long sleepTime = radioInstance.getMinSleepTime();
//				 long commTime =
//				 radioInstance.getCommunicationTime();**********/
//				
//				 radioInstance.computeRadioOnTime();
//				 long commOnTime = radioInstance.getRadioOnTime();
//				 long commOffTime = radioInstance.getRadioOffTime();
//				 Administrator.getAdministratorInstance().setRadioTimer(commOffTime,
//				 commOnTime);
//				 RadioTimer timerInstance =
//				 RadioTimer.getInstance(commOffTime, commOnTime);
//				 /*Administrator.getAdministratorInstance().setRadioTimer(666,
//				 10);
//				 RadioTimer timerInstance = RadioTimer.getInstance(666, 10);*/
//				 //timerInstance.start();
//				 }
//				 try {
//				 Thread.currentThread().sleep(10000);
//				 }
//				 catch(InterruptedException ie) {
//				 System.out.println("CASCADE: Inserted delay is interrupted");
//				 }
/*-------------------------------------------------------------------------------------------------------------*/

				 
				// command list is ready to send to other sensor nodes
				for (int i = 0; i < commandList.size(); i++) {
					try {
						String netAddress = commandList.get(i).getElement(1)
								.tostring();
						if (myAddress.equals(netAddress)) {
							System.out.println("Command to create--->"
									+ commandList.get(i).tostring());
							ServerAdministrator.getAdministratorInstance()
									.receiveCommand(commandList.get(i));
						}
					} catch (Exception ex) {
						System.out
								.println("CASCADE: failed while creating operator");
					}
					try {
						Thread.currentThread().sleep(5);
					} catch (InterruptedException ie) {
						System.out.println("CASCADE: Interrupted thread" + ie);
					}
				}

				for (int i = 0; i < commandList.size(); i++) {
					try {
						String netAddress = commandList.get(i).getElement(1)
								.tostring();
						int tmpOp;

						if (!myAddress.equals(netAddress)) {
							System.out.println("Command to create--->"
									+ commandList.get(i).tostring());
							DatagramSocket socket = new DatagramSocket();
							try {
								byte[] byteData;
								ByteArrayOutputStream bout = new ByteArrayOutputStream();
								ObjectOutputStream oos = new ObjectOutputStream(
										bout);
								oos.writeObject(commandList.get(i));
								byteData = bout.toByteArray();
								DatagramPacket packet = new DatagramPacket(
										byteData, byteData.length,
										InetAddress.getByName(netAddress), 110);
								socket.send(packet);
							} catch (Exception ex) {
								System.out.println("CASCADE: " + ex);
							} finally {
								socket.close();
								socket = null;
							}
						}
					} catch (IOException ex) {
						System.out
								.println("CASCADE: problem in radiogram connection");
						ex.printStackTrace();
					} catch (ParsingException ex) {
						System.out
								.println("CASCADE: problem in sending command list to sensor nodes");
					} catch (Exception ex) {
						System.out
								.println("CASCADE: problem in radiogram connection");
						ex.printStackTrace();
					}
					try {
						Thread.currentThread().sleep(50);
					} catch (InterruptedException ie) {
						System.out.println("CASCADE: Interrupted thread" + ie);
					}
				}

			} else {
				System.out.println("CASCADE: query " + name
						+ " is already running");
			}
		} else {
			System.out.println("CASCADE: no query with " + name);
		}
	}

   /**
    * This method creates the command to be executed at the node
    * @param node A node
    * @param graph Steam graph
    * @return created command as record
    * @throws Exception
    */

   DRecord createCommand(StreamNode node, StreamGraph graph) throws Exception
   {
       // pre defined format of command
       DRecord command = new DRecord(9);

       // command type
       DString create = new DString("create");
       command.setElement(0, create);

       // sensor node
       String sensorNode = node.getSensorNode();

       // check if it is valid sensor node
		if (!sensorNode.equals("")) {
			DString senseNode = new DString(sensorNode);
			command.setElement(1, senseNode);
			String myAddress = null;
			try {
				myAddress = NetworkAddress.getAddress().getHostAddress();
			} catch (Exception e) {
				System.out.println("CASCADE: unable to fetch information about network interfaces");
			}
           
           if(myAddress.equals(senseNode.tostring())) {
               isnodeCreated.put(sensorNode, Boolean.TRUE);
               int tmpOp = 0;
               if(sensorNumOperators.containsKey(myAddress)) {
                   tmpOp = sensorNumOperators.get(myAddress);
               }
               tmpOp++;
               sensorNumOperators.put(myAddress, tmpOp);
               sensorNumOperatorsCreated.put(myAddress, tmpOp);
           }
           else {
               isnodeCreated.put(sensorNode, Boolean.FALSE);
               int tmpOp = 0;
               if(sensorNumOperators.containsKey(sensorNode)) {
                   tmpOp = sensorNumOperators.get(sensorNode);
               }
               tmpOp++;
               sensorNumOperators.put(sensorNode, tmpOp);
           }
       } else {
           throw new Exception("CASCADE:sensor node identifier is empty");
       }
       // stream operator ID
       try {
           String nodeId = Integer.toString(node.getId());
           DString id = new DString(nodeId);
           command.setElement(2, id);
       }
       catch (NumberFormatException ne) {
           throw new Exception("CASCADE:invalid stream node id ");
       }

       // number input channels
       List<StreamEdge> inputs = graph.getInEdges(node);
       short inChannels = (short) inputs.size();
       DInteger16 numInChannels = new DInteger16(inChannels);
       command.setElement(3, numInChannels);

       // array of records which each record has three elements channel Id, data type and sender Id
       try {
           DRecord inputChannels = new DRecord(inputs.size());
           for (int i = 0; i < inputs.size(); i++) {
               StreamEdge inEdge = inputs.get(i);
               DRecord inChannel = new DRecord(3);
               // channel id
               String chid = Integer.toString(inEdge.getId());
               DString id = new DString(chid);
               inChannel.setElement(0, id);

               // data type
               String type = inEdge.getType();
               DString datatype = new DString(type);
               inChannel.setElement(1, datatype);

               // sender ID
               String sid = Integer.toString(inEdge.getSource().getId());
               DString senderID = new DString(sid);
               inChannel.setElement(2, senderID);

               // put in DRecord
               inputChannels.setElement(i, inChannel);
           }
           command.setElement(4, inputChannels);
       }
       catch (NumberFormatException e) {
           throw new Exception("CASCADE: invalid number format");
       }

       // number output channels
       List<StreamEdge> outputs = graph.getOutEdges(node);
       short outChannels = (short) outputs.size();
       DInteger16 numOutChannels = new DInteger16(outChannels);
       command.setElement(5, numOutChannels);

       // array of records which each record has three elements channel Id, data type and sneder Id
       try {
           DRecord outputChannels = new DRecord(outputs.size());
           for (int i = 0; i < outputs.size(); i++) {
               StreamEdge outEdge = outputs.get(i);
               DRecord outChannel = new DRecord(3);
               // channel id
               String chid = Integer.toString(outEdge.getId());
               DString id = new DString(chid);
               outChannel.setElement(0, id);

               // data type
               String type = outEdge.getType();
               DString datatype = new DString(type);
               outChannel.setElement(1, datatype);

               // receiver ID
               String sid = Integer.toString(outEdge.getTail().getId());
               DString senderID = new DString(sid);
               outChannel.setElement(2, senderID);

               // put in DRecord
               outputChannels.setElement(i, outChannel);
           }
           command.setElement(6, outputChannels);
       }
       catch (NumberFormatException e) {
           throw new Exception();
       }

       // operator type
       String opType = node.getOp();
       if (opType.startsWith(Constants.PACKAGE_NAME)) {
           opType = opType.substring(Constants.PACKAGE_NAME.length()+1);
       }
       // check whether the operator type is valid or not
       DString name = new DString(opType);
       if(name.tostring().equals("StreamNodeFilter")) {
           name = ((StreamNodeFilter)node).getFilterName();
       }
       command.setElement(7, name);


       // property list - put keys in one array and values in another one to pass
       // reason for 6 is that we put scheduling semantic and 5 parameters to the property list
       DRecord plist= new DRecord(node.propertyList.size() + 6);
       String type;
       int idx = 0;

       // scheduling semantic
       try {
           String sched;
           if (node.isSenseOp()) {
        	   String scheduling = (String)node.propertyList.get("sched");
        	   if (scheduling != null && scheduling.compareTo("EventTriggered") == 0)
        		   sched = "EventTriggered";
        	   else
        		   sched = "TimeTriggered";
           } else {
               sched = "EventTriggered";
           }
           // check wether it is valid scheduling semantic
           if (!(sched.equals("TimeTriggered") || (sched.equals("EventTriggered"))))
               throw new Exception("Invalid scheduling semantic");
           DString schedSem = new DString(sched);
           DRecord property_sched = new DRecord(3);
           property_sched.setElement(0, new DString("schedulingSemantic"));
           property_sched.setElement(1, schedSem);
           type = typeMap.get(sched.getClass().getName());
           property_sched.setElement(2, new DString(type));
           plist.setElement(idx, property_sched);
           idx++;
       }
       catch (Exception e) {
           throw new Exception("problem in getting scheduling semantic");
       }

       // scheduling parameters
       // start time
       DInteger32 starttime = new DInteger32();
       DRecord property_start = new DRecord(3);
       if (node.propertyList.containsKey("start")) {
           int start = Integer.parseInt(node.propertyList.get("start").toString());
           starttime.setValue(start);
           Object o = start;
           type = typeMap.get(o.getClass().getName());
       }else {
           starttime.setValue(0);
           type = "DInteger64";
       }
       property_start.setElement(0, new DString("start"));
       property_start.setElement(1, starttime);
       property_start.setElement(2, new DString(type));
       plist.setElement(idx, property_start);
       idx++;

       // repeat
       DInteger32 repeat = new DInteger32();
       DRecord property_repeat = new DRecord(3);
       if (node.propertyList.containsKey("repeat")) {
           int rep = Integer.parseInt(node.propertyList.get("repeat").toString());
           repeat.setValue(rep);
           // extract its type
           Object o = rep;
           type = typeMap.get(o.getClass().getName());
       } else {
           repeat.setValue(500000);
           type = "DInteger32";
       }
       property_repeat.setElement(0, new DString("repeat"));
       property_repeat.setElement(1, repeat);
       property_repeat.setElement(2, new DString(type));
       plist.setElement(idx, property_repeat);
       idx++;

       // interval
       DInteger64 interval = new DInteger64();
       DRecord property_interval = new DRecord(3);
       if (node.propertyList.containsKey("interval")) {
           long inter = Long.parseLong(node.propertyList.get("interval").toString());
           interval.setValue(inter);
           // extract its type
           Object o = inter;
           type = typeMap.get(o.getClass().getName());
       } else {
           interval.setValue(5000);
           type = "DInteger64";
       }
       property_interval.setElement(0, new DString("interval"));
       property_interval.setElement(1, interval);
       property_interval.setElement(2, new DString(type));
       plist.setElement(idx, property_interval);
       idx++;

       // channel number
       DInteger32 minChannel = new DInteger32();
       DRecord property_channel = new DRecord(3);
       if (node.propertyList.containsKey("minChannel")) {
           int channels = (Integer) node.propertyList.get("minChannel");
           minChannel.setValue(channels);
           // extract its type
           Object o = channels;
           type = typeMap.get(o.getClass().getName());
       } else {
           minChannel.setValue(1);
           type = "DInteger32";
       }
       property_channel.setElement(0, new DString("minChannel"));
       property_channel.setElement(1, minChannel);
       property_channel.setElement(2, new DString(type));
       plist.setElement(idx, property_channel);
       idx++;

       // timeout for time triggered events
       DInteger64 timeout = new DInteger64();
       DRecord property_timeout = new DRecord(3);
       if (node.propertyList.containsKey("timeout")) {
           int time = Integer.parseInt(node.propertyList.get("timeout").toString());
           timeout.setValue(time);
           // extract its type
           Object o = time;
           type = typeMap.get(o.getClass().getName());
       } else {
           timeout.setValue(3600000);
           type = "DInteger64";
       }
       property_timeout.setElement(0, new DString("timeout"));
       property_timeout.setElement(1, timeout);
       property_timeout.setElement(2, new DString(type));
       plist.setElement(idx, property_timeout);
       idx++;

       Set<String> keyset = node.propertyList.keySet();
       Iterator iter;
       iter = keyset.iterator();
       while(iter.hasNext()) {
           String key = (String) iter.next();
           Object o = node.propertyList.get(key);
           String value = o.toString();
           String datatype = typeMap.get(o.getClass().getName());
           DRecord property = new DRecord(3);
           property.setElement(2, new DString(datatype));
           property.setElement(1, new DString(value));
           property.setElement(0, new DString(key));
           plist.setElement(idx, property);
           idx++;
       }

       command.setElement(8, plist);

       return command;
   }

   /**
    * Kill a query in the system and in network
    * @param name query name
    */
   void kill(String name){
      if(queries.containsKey(name)) {
         StreamGraph graph = queries.get(name);
         if (graph.getStatus()) {
           System.out.println("CASCADE: kill " + name);
           graph.setStatus(false);
           // TODO: connect to runtime system
         } else {
           System.out.println("CASCADE: query " + name + " is not running");
         }
      } else {
        System.out.println("CASCADE: no query with " + name);
      }
   }

   /**
    * Check status of query which it is running or not
    * @param name query name
    * @return its running status
    */
   boolean getStatus(String name){
      boolean status = false;
      if(queries.containsKey(name)) {
          status = queries.get(name).getStatus();
          if(status){
            System.out.println("CASCADE: query " + name + " is running");
          }else {
            System.out.println("CASCADE: query " + name + " is not running");
          }
      }else {
        System.out.println("CASCADE: no query with " + name);
      }
       return status;
   }

   /**
    * Visualize stream graph of query
    * @param name query name
    * @throws FileNotFoundException
    */
   void visualize(String name){
       try{
           if(queries.containsKey(name)) {
                System.out.println("CASCADE: visualize " + name);
                queries.get(name).visualize();
           }else {
                System.out.println("CASCADE: no query with " + name);
           }
       }
       catch(Exception ex){
           System.out.println("CASCADE: problem visualizing graph" + name);
       }
   }

   /**
    * Print stream graph of query
    * @param name query name
    */
   void print(String name) {
      if(queries.containsKey(name)) {
         System.out.println("CASCADE: print " + name);
         queries.get(name).print();
      } else {
        System.out.println("CASCADE: no query with " + name);
      }
   }


   /**
    * Define new query with its assigned stream graph in system
    * @param name query name
    * @param graph assigned stream graph
    */
   void defineQuery(String name, StreamGraph graph, boolean isSensorStreaming) {
      if(!queries.containsKey(name)) {
         System.out.println("CASCADE: define query " + name);
         queries.put(name,graph);
         graph.computeDataType();
         graph.computeBandwidth();

         //Allocate nodes to sensor:
         //1)With Multiway Cut
         //2)Without Multiway cut
         //ContextAllocate context;
         if(isSensorStreaming) {
             contextTable.put(name, new AllocateWithMWCut());
         } else {
             contextTable.put(name,new AllocateWithoutMWCut());
         }
         long timeStamp = System.currentTimeMillis();
         for(int cnt = 0; cnt < 10000; cnt++) {
             contextTable.get(name).allocateToSensor(graph);
         }
         System.out.println("Time Taken to compute Multiway-cut is -->" + (System.currentTimeMillis() - timeStamp)/10000.00);
      } else {
        System.out.println("CASCADE: query with " + name + " is already defined!");
      }
   }

   /**
    * load the query from the given file
    * @param filename file with query
    */
   void load(String filename) {
       try {
             CharStream input = new ANTLRFileStream(filename);
             TLexer lex = new TLexer(input);
             CommonTokenStream tokens = new CommonTokenStream(lex);
	     TParser parser = new TParser(tokens);
	     parser.program();
       }
       catch(Exception e) {
            System.out.println("CASCADE: cannot load file " + filename);
       }
   }


   /**
    * This method notifies the gateway whenever nodes get created for the query
    * If gateway has received notifications from all the required nodes then
    * it sends the "commit" message to all the nodes which will start scheduler
    * to run the query
    * @param node node that sent a creation notification
    */
   public synchronized void notifyGateway(String node) {
       if(isnodeCreated.containsKey(node)) {
            isnodeCreated.put(node, Boolean.TRUE);
       }
       int tmpOp = 0;
       if(sensorNumOperatorsCreated.containsKey(node)) {
           tmpOp = sensorNumOperatorsCreated.get(node);
       }
       tmpOp++;
       sensorNumOperatorsCreated.put(node, tmpOp);

       boolean commitFlag = false;
       if(sensorNumOperators.size() == sensorNumOperatorsCreated.size()) {
           commitFlag = true;
           Set<String> keySet = sensorNumOperators.keySet();
           for(String key : keySet) {
               if(sensorNumOperators.get(key) != sensorNumOperatorsCreated.get(key)) {
                   commitFlag = false;
               }
           }
       }
       // if all nodes have been created then commit
		if (!isnodeCreated.containsValue(Boolean.FALSE) && commitFlag) {
			DRecord message = new DRecord(1);
			DString data = new DString("Commit");
			Set<String> keySet = sensorNumOperators.keySet();
			for (String key : keySet) {
				String myAddress = null;
				try {
					myAddress = NetworkAddress.getAddress().getHostAddress();
				} catch (Exception e) {
					System.out
							.println("CASCADE: unable to fetch information about network interfaces");
				}
				if (key.compareTo(myAddress) == 0) {
					continue;
				}
				try {
					message.setElement(0, data);
					byte[] byteData = null;
					ByteArrayOutputStream bout = new ByteArrayOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(bout);
					oos.writeObject(message);
					byteData = bout.toByteArray();
					DatagramSocket socket = new DatagramSocket();
					DatagramPacket packet = new DatagramPacket(byteData,
							byteData.length, InetAddress.getByName(key), 110);
					socket.send(packet);
					socket.close();

					// start scheduler on basestation/server
					if (!Scheduler.getSchedulerInstance().isAlive()) {
						Scheduler.getSchedulerInstance().startSchedule();
						// RadioTimer.getInstance().start();
					}
					try {
						Thread.currentThread().sleep(20);
					} catch (InterruptedException ie) {
						System.out.println("CASCADE: interrupted thread!");
					}
				} catch (ParsingException pe) {
					System.out
							.println("CASCADE: Data parsing failed at gateway during 2-phase commit");
				} catch (IOException pi) {
					System.out
							.println("CASCADE: 2-phase commit failed from Gateway");
					pi.printStackTrace();
				}
			}
		}
	}

   /**
    * This method returns all the queries in the system
    * @return query-Streamgraph table
    */
   Map<String, StreamGraph> getQueries() {
       return queries;
   }

   /**
    * This method returns the list of contexts used during nodes allocation
    * for each query in the system
    * @return context table
    */
   Map<String, Allocator> getContext() {
       return contextTable;
   }
};
