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

import java.util.*;

/**
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public interface Allocator {
    public void allocateToSensor(StreamGraph g);
    public List<StreamEdge> getCutEdges();
}
/**
 * This method allocates each non terminal node to a specific sensor node
 * without using Multiway cut
 */
class AllocateWithoutMWCut implements Allocator {
    private List<StreamEdge> cutEdges;

    public AllocateWithoutMWCut() {
        cutEdges = new LinkedList<StreamEdge>();
    }
    public void allocateToSensor(StreamGraph g){
        List<StreamNode> nodes = g.getNodes();
        StreamNode sink = g.getSink();
         for (StreamNode node: nodes) {
           if (node.propertyList.containsKey("node")) {
               node.setSensorNode((String) node.propertyList.get("node"));
           } else {
               node.setSensorNode(sink.getSensorNode());
           }
        }
        this.cutEdges = computeCutEdges(g);       
    }

    /**
     * This method computes the cut-edges
     * @param graph stream graph
     * @return list of cut-edges
     */
    private List<StreamEdge> computeCutEdges(StreamGraph graph) {
        //List<StreamEdge> cEdges = new LinkedList<StreamEdge>();
        List<StreamEdge> edges = graph.getEdges();
        int idx=0;
        int currNodeId = edges.get(0).getSource().getId();
        for(StreamEdge e : edges) {
            //int fNode = e.getSource().getId() - currNodeId;
            String fNode = e.getSource().getSensorNode();
            String tNode = e.getTail().getSensorNode();
            if (fNode.compareTo(tNode) != 0){
                cutEdges.add(idx++, e);
            } else {
                continue;
            }
        }
        return cutEdges;
    }

    /**
     * This method returns the cut-edges after operator allocation on nodes
     * @return list of cut-edges
     */
    public List<StreamEdge> getCutEdges() {
            return cutEdges;
    }
}

/**
 * This method finds the sensor node location of each non terminal node by using
 * multiway cut
 */
class AllocateWithMWCut implements Allocator {

    List<StreamEdge> cutEdges;

    public AllocateWithMWCut() {
        cutEdges = new LinkedList<StreamEdge>();
    }


    /**
     * Allocate each node to one of the sensor nodes
     * @param g Stream Graph
     */
    public void allocateToSensor(StreamGraph g){
      List<StreamNode> nodes = g.getNodes();
      List<StreamEdge> edges = g.getEdges();


      // find stream operators that are allocated on
      // the same sensor node. A disjoint sets structure
      // is used to cluster the stream operators.
      int numNodes = nodes.size();
      DisjointSets djs = new DisjointSets(numNodes);
      for (int u=0 ; u < numNodes; u++) {
          if(nodes.get(u).getSensorNode().equals("")) {
              continue;
          }
          for (int v=0; v < numNodes; v++) {
              StreamNode uNode = nodes.get(u);
              StreamNode vNode = nodes.get(v);
              if(uNode.getSensorNode().equals(vNode.getSensorNode())){
                  djs.union(u, v);
              }
          }
      }

      // compute number of disjoint sets
      djs.computePartitionNumbers();

      // determine all terminal nodes for multi way cut
      // a terminal node is the set of stream operators
      // placed on a sensor node
      int numTerminals = 0;
      int terminalNodes[] = new int[djs.getNumDisjointSets()];
      for (int u=0 ; u < numNodes; u++) {
          if(nodes.get(u).getSensorNode().equals("")) {
              continue;
          }
          if (djs.find(u)==u) {
             terminalNodes[numTerminals++] = djs.getPartitionNumber(u);
          }
      }

      // construct muliway-cut instance
      int nodeNum = djs.getNumDisjointSets();
      MultiwayCut problemMC = new MultiwayCut(nodeNum, numTerminals, terminalNodes);
      //int currStartNode = edges.get(0).getSource().getId();
      int currStartNode = 0;
      for(StreamEdge edge : edges ) {
          int u = djs.getPartitionNumber(edge.getSource().getId() - currStartNode);
          int v = djs.getPartitionNumber(edge.getTail().getId() - currStartNode);
          problemMC.addEdge(u,v,(int)edge.getBandwidth());
      }

      // compute multiway cut
      problemMC.compute();

      // assign stream operations to nodes
      for(int u=0;u<numNodes;u++) {
         // find sensor node for current node
         int partition = problemMC.getPartition(djs.getPartitionNumber(u));
         int v = djs.getLeader(partition);

         String sensorNode = nodes.get(v).getSensorNode();
         nodes.get(u).setSensorNode(sensorNode);
      }

      // compute cut edges
      this.cutEdges = computeCutEdges(g, djs, problemMC);
    }

    /**
     * This method computes the cut-edges
     * @param graph stream graph
     * @param djs disjoint sets
     * @param instance multiway cut instance
     * @return list of cut-edges
     */
    private List<StreamEdge> computeCutEdges(StreamGraph graph, DisjointSets djs, MultiwayCut instance) {
        List<StreamEdge> edges = graph.getEdges();
        int idx=0;
        //int currNodeId  = edges.get(0).getSource().getId();
        int currNodeId = 0;
        for(StreamEdge e : edges) {
            int fNode = e.getSource().getId() - currNodeId;
            int tNode = e.getTail().getId() - currNodeId;
            int fPartition = instance.getPartition(djs.getPartitionNumber(fNode));
            int tPartition = instance.getPartition(djs.getPartitionNumber(tNode));
            if (fPartition != tPartition){
                cutEdges.add(idx++, e);
            } else {
                continue;
            }
        }
        return cutEdges;
    }

    /**
     * This method returns the cut-edges after operator allocation on nodes
     * @return list of cut-edges
     */
    public List<StreamEdge> getCutEdges() {
            return cutEdges;
    }
}