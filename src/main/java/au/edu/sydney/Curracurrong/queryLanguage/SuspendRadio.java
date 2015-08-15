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

import au.edu.sydney.Curracurrong.datatype.DRecord;
import au.edu.sydney.Curracurrong.datatype.DString;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

import au.edu.sydney.Curracurrong.runtime.server.ServerCommunicator;
import au.edu.sydney.Curracurrong.util.NetworkAddress;

/**
 * This class is used to compute the time for which radio communication will be
 * turned off to reduce the battery usage.
 *
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public class SuspendRadio {
    /**
     * singleton instance of the class SuspendRadio
     */
    private static SuspendRadio instance;
    /**
     * Table that stores the free memory for all nodes in system
     */
    private Map<String, Long> freeMemory;
    /**
     * Table that stores the cut-edges of Streamgraph and its bandwidth
     */
    private Map<StreamEdge, Double> cutBandwidth;
    /**
     * Table that stores the sleep time for each node based on which the
     * min sleep time will be considered for all nodes
     */
    Map<String, Long> onTime;
    /**
     * bandwidth ratio = (total-bandwidth for each cut-edge)/(physical bandwidth limitation of the Sun SPOT radio)
     */
    private double bandwidthRatio;

    private Map<String, Long> timePeriods;

    private long timePeriod;

    private long radioOnTime;

    private final boolean isMultihop = false;

    /**
     * This method returns the singleton instance of SuspendRadio
     * @return instance
     */
    public static SuspendRadio getInstance() {
        if(instance == null) {
            instance = new SuspendRadio();
        }
        return instance;
    }

    /**
     * private constructor
     */
    private SuspendRadio() {
        freeMemory = new HashMap<String, Long>();
        cutBandwidth = new HashMap<StreamEdge, Double>();
    }

   /**
    * compute total bandwidth for each cut-edge in system
    * also computes the bandwidth ratio
    **/
   private void computeCutBandwidth(){
       ControlCentre CCinstance = ControlCentre.getInstance();
       Collection <String> tmpQueries = CCinstance.getQueries().keySet();
       Iterator<String> itr = tmpQueries.iterator();
       Map <String, Allocator> tmpContext = CCinstance.getContext();
       double totalBw=0;
       while(itr.hasNext()){
           String q = itr.next();
           List<StreamEdge> cutEdges = tmpContext.get(q).getCutEdges();
           for(StreamEdge e : cutEdges){
               double edgeBW = e.getBandwidth();
               if(cutBandwidth.containsKey(e)) {
                   double bw = cutBandwidth.get(e);
                   cutBandwidth.put(e, bw+edgeBW);
               } else {
                   cutBandwidth.put(e, edgeBW);
               }
               totalBw = totalBw + edgeBW;
           }
       }
       System.out.println("Total cut bandwidth---->>>" +totalBw + "\n\n");
       //bandwidthRatio = totalBw / 20; //20-physical limitation for transfer
       bandwidthRatio = totalBw / 31; //20-physical limitation for transfer
   }

   /**
    * This method sends the "FreeMem" message to all the nodes acquiring
    * free memory
    */
	public void getSensorFreeMem() {
		// add free memory for basestation
		String myAddress = null;
		try {
			myAddress = NetworkAddress.getAddress().getHostAddress();
		} catch (Exception e) {
			System.out.println("CASCADE: unable to fetch information about network interfaces");
		}
		freeMemory.put(myAddress, Runtime.getRuntime().freeMemory());

		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket();
			socket.setBroadcast(true);
		} catch (SocketException e) {
			System.out.println("CASCADE: Unable to create DatagramSocket - aquiring Free memory failed");
			e.printStackTrace();
		}

		for (int i = 0; i < 2; i++) {
			DRecord message = new DRecord(1);
			try {
				message.setElement(0, new DString("FreeMem"));
				byte[] byteData;
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(bout);
				oos.writeObject(message);
				byteData = bout.toByteArray();

				System.out.println("Sending timer information to all sensor"
						+ message.tostring());

//				DatagramPacket packet = new DatagramPacket(byteData,
//						byteData.length,
//						InetAddress.getByName("255.255.255.255"), 110);
				// note: this is a CNAME put into the /etc/hosts file for now
				for (InetAddress addr : InetAddress.getAllByName("curracurrong.cluster")) {
					DatagramPacket packet = new DatagramPacket(byteData, byteData.length, addr, 110);
					socket.send(packet);
				}
				socket.close();
			} catch (ParsingException pe) {
				System.out.println("CASCADE: failed parsing while aquiring free memory");
			} catch (IOException pi) {
				System.out.println("CASCADE: Aquiring Free memory failed");
			}
		}
	}

     /**
      * This method makes the entry into the table to store free memory details
      * received from each node
      * @param id Node id
      * @param freeMem free memory
      */
     public synchronized void addFreeMemory(String id, long freeMem) {
         if(!freeMemory.containsKey(id)) {
             freeMemory.put(id, freeMem);
         }
     }

     /**
      * Compute the sleep time for each node based on the free memory
      */
     /*public void computeSleepTime() {
         sleepTime = new HashMap<String, Long>();
         // compute bandwidth for all cut edges
         computeCutBandwidth();

         // compute sleep time for each node if it is the source of cut edge
         Collection<StreamEdge> tmpCutEdges = cutBandwidth.keySet();
         Iterator<StreamEdge> itrEdge;
         Iterator<String> itrSenseNode = freeMemory.keySet().iterator();
         Hashtable<Integer, String> sensorNodeMap = Communicator.getCommunicatorInstance().getSensorNodeMap();
         Iterator<Integer> itrMap;

         // for each sensor node
         while(itrSenseNode.hasNext()) {
             String tmpSenseNode = itrSenseNode.next();
             double totalBw = 1;
             itrMap = sensorNodeMap.keySet().iterator();

             // for each node deployed on given sensor node
             while(itrMap.hasNext()) {
                 Integer nodeId = itrMap.next();
                 if(sensorNodeMap.get(nodeId).equals(tmpSenseNode)) {
                     itrEdge = tmpCutEdges.iterator();

                     // if node is a source of the cut edge compute total cost/bandwidth for the sensor node
                     while(itrEdge.hasNext()) {
                         StreamEdge edge = itrEdge.next();
                         int source = edge.getSource().getId();
                         if(source == nodeId) {
                             totalBw = totalBw + cutBandwidth.get(edge);
                         }
                     }
                 }
             }
             if(totalBw > 1) {
                 totalBw--;
             }
             long time = (long) (freeMemory.get(tmpSenseNode)/ (2*totalBw));
             sleepTime.put(tmpSenseNode, time);
         }
     }*/

     public void computeRadioOnTime() {
         onTime = new HashMap<String, Long>();
         timePeriods = new HashMap<String, Long>();
         computeCutBandwidth();
         Collection<StreamEdge> tmpCutEdges = cutBandwidth.keySet();
         Iterator<StreamEdge> itrEdge;
         Iterator<String> itrSenseNode = freeMemory.keySet().iterator();
         Hashtable<Integer, String> sensorNodeMap = ServerCommunicator.getCommunicatorInstance().getSensorNodeMap();
         Iterator<Integer> itrMap;

         // for each sensor node
         while(itrSenseNode.hasNext()) {
             String tmpSenseNode = itrSenseNode.next();
             double totalBw = 1;
             long totalTimePeriod;
             long bytesToSend;
             itrMap = sensorNodeMap.keySet().iterator();

             // for each node deployed on given sensor node
             while(itrMap.hasNext()) {
                 Integer nodeId = itrMap.next();
                 if(sensorNodeMap.get(nodeId).equals(tmpSenseNode)) {
                     itrEdge = tmpCutEdges.iterator();

                     // if node is a source of the cut edge compute total cost/bandwidth for the sensor node
                     while(itrEdge.hasNext()) {
                         StreamEdge edge = itrEdge.next();
                         int source = edge.getSource().getId();
                         if(source == nodeId) {
                             totalBw = totalBw + cutBandwidth.get(edge);
                         }
                     }
                 }
             }
             if(totalBw > 1) {
                 totalBw--;
             }
             totalTimePeriod = (long) (freeMemory.get(tmpSenseNode)/(2 * totalBw));
             bytesToSend = (long) (totalTimePeriod * totalBw);
             long time;
             long nodes = freeMemory.size();
             System.out.println("Number of nodes considered........" + nodes);
             if(isMultihop) {
                time = bytesToSend * (nodes * (nodes - 1))/ 12500;
             } else {
                time = bytesToSend / 12500;
             }
             timePeriods.put(tmpSenseNode, totalTimePeriod);
             onTime.put(tmpSenseNode, time);
         }
         computeMinRadioOnTime();
     }

     /**
      * This method returns the minimum sleep time that is to be set for each node
      * @return sleep-time
      */
     /*public long getMinSleepTime() {
         long sleep=Long.MAX_VALUE;
         Iterator<String> itrNode = sleepTime.keySet().iterator();

         while(itrNode.hasNext()) {
             long tmpSleep = sleepTime.get(itrNode.next());
             if(sleep > tmpSleep) {
                 sleep = tmpSleep;
             }
         }
         return sleep;
     }*/

     private void computeMinRadioOnTime() {
         long tmpTime = Long.MAX_VALUE;
         Iterator<String> itrNode = onTime.keySet().iterator();

         while(itrNode.hasNext()) {
             String node = itrNode.next();
             long tmpOn = onTime.get(node);
             if(tmpTime > tmpOn) {
                 tmpTime = tmpOn;
                 timePeriod = timePeriods.get(node);
             }
         }
         radioOnTime = tmpTime;
     }

     public long getRadioOnTime() {
         return radioOnTime;
     }

     /**
      * This method returns the bandwidth ratio
      * @return bandwidth ratio
      */
     private double getBandwidthRatio() {
         return bandwidthRatio;
     }

     /**
      * This method returns the communication time for each node
      * @return communication-time
      */
     /*public long getCommunicationTime() {
         long commTime=0;
         long sleep = getMinSleepTime();
         commTime = (long) (sleep - (sleep / (1 - getBandwidthRatio())));
         return commTime;
     }*/
     public long getRadioOffTime() {
         long radioOffTime=0;         
         radioOffTime = timePeriod - getRadioOnTime();
         return radioOffTime;
     }

     /**
      * This method returns the Node vs free-memory table
      * @return free memory table
      */
     public Map<String, Long> getFreeMemory() {
         return freeMemory;
     }
}
