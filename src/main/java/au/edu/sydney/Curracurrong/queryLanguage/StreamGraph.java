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
import java.io.*;
import java.awt.image.*;

/**
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */

/**
 * System runs multiple queries at the same time
 * All the stream graphs contain unique node and edge ids.
 * NodeEdgeId is the Singleton class for Unique Node ids
 * and Edge ids for Stream Graph.
 */
class NodeEdgeId {
    private static NodeEdgeId instance = null;
    /**
     * Unique node id for stream graphs
     */
    private int nodeId;

    /**
     * Unique edge id for stream graphs
     */
    private int edgeId;

    protected NodeEdgeId(){
        nodeId = 0;
        edgeId = 0;
    }
    /**
    * singleton access method
    * @return instance of class
    */
    public static NodeEdgeId getInstance(){
        if(instance == null){
            instance = new NodeEdgeId();
        }
        return instance;
    }

    public void setUniqueNodeId(int id){
        NodeEdgeId.instance.nodeId = id;
    }
    public int getUniqueNodeId() {
        return nodeId;
    }
    public void setUniqueEdgeId(int id){
        NodeEdgeId.instance.edgeId = id;
    }
    public int getUniqueEdgeId(){
        return edgeId;
    }
    
}


class StreamGraph implements Constants  {
   /**
    * Set of nodes for the stream graph
    */
   List<StreamNode> nodes;             

   /**
    * Set of edges for the stream graph
    */
   List<StreamEdge> edges;             

   /**
    * Sink node for stream graph added at server
    */
   protected StreamNode sink;          

   /**
    * Set of all source nodes for the stream graph
    */
   protected List<StreamNode> sources; 

   /**
    * Status of the stream graph:
    * true if the query is currently being executed,
    * false otherwise
    */
   boolean running;                   
            

   StreamGraph() {
      nodes = new LinkedList<StreamNode>();
      edges = new LinkedList<StreamEdge>();
      sources = new LinkedList<StreamNode>();
      running = false;
   }

   /**
    * This method adds a new StreamNode to the graph
    * and sets the unique node id
    * @param node A stream node that is to be added
    */
   void addNode(StreamNode node) {
       assert node != null;
       int id = NodeEdgeId.getInstance().getUniqueNodeId();
       node.setId(id);
       id++;
       NodeEdgeId.getInstance().setUniqueNodeId(id);
       nodes.add(node);          
   }

   /**
    * This method adds a new streamEdge to the graph
    * and sets the unique edge id
    * @param source source node of the edge
    * @param tail tail node of the edge
    */
   void addEdge(StreamNode source, StreamNode tail) {
       assert source != null && tail != null;
       StreamEdge edge = new StreamEdge(source,tail);
       int edgeId = NodeEdgeId.getInstance().getUniqueEdgeId();       
       edge.setId(edgeId);
       edgeId++;
       NodeEdgeId.getInstance().setUniqueEdgeId(edgeId);
       edges.add(edge);       
   }

   /**
    * This method is created for test cases,
    * where we can initialize capacity at creation time
    * @param source A source node of the edge
    * @param tail A tail node of the edge
    * @param bandwidth Bandwidth of the edge
    */
   void addEdge(StreamNode source, StreamNode tail, double bandwidth) {
       StreamEdge edge = new StreamEdge(source,tail);       
       int edgeId = NodeEdgeId.getInstance().getUniqueEdgeId();       
       edge.setId(edgeId);
       edgeId++;
       NodeEdgeId.getInstance().setUniqueEdgeId(edgeId);
       edge.setBandwidth(bandwidth);
       edges.add(edge);
   }

   /**
    * This method returns the list of stream nodes
    * @return list of stream nodes
    */
   public List<StreamNode> getNodes() {
       return nodes;
   }

   /**
    * This method returns the list of stream edges
    * @return list of stream edges
    */
   public List<StreamEdge> getEdges() {
       return edges;
   }

   /**
    * This method assigns the sink node to the graph
    * @param sink sink node
    */
   void setSink(StreamNode sink) {
      this.sink = sink;
   }

   /**
    * This method adds the source node to the graph.
    * Source node would be one of the sensors
    * @param source sensor node
    */
   void addSource(StreamNode source) {
      sources.add(source);
   }

   /**
    * This method returns the sink node of the graph
    * @return sink node
    */
   StreamNode getSink() {
      return sink;
   }

   /**
    * This method sets the running status of the graph;
    * True if query is in execution mode and false otherwise
    * @param stat execution status of graph
    */
   void setStatus(boolean stat) {
      running = stat;
   }

   /**
    * This method returns the running status of the graph
    * @return True if graph is in execution mode, false otherwise
    */
   boolean getStatus() {
      return running;
   }

   /**
    * visualize the graph by using grappa package
    * @throws FileNotFoundException
    */
   void visualize() throws FileNotFoundException {
       //create unique tmp dot file

       Random r = new Random();
       String name = TEMP_DIR + "q_" + r.nextInt(5000) + ".dot";
       FileOutputStream fstream = new FileOutputStream(name);
       PrintStream out = new PrintStream(fstream);

       out.println("digraph G {");
       for (StreamNode node : nodes) {
          String color;
          if (node == sink ) {
             color = "blue";
          } else if (sources.contains(node)) {
             color = "red";
          } else {
             color = "black";
          }
          out.println("n"+node.getId()+" [label=\""+node.getOp()+node.toStringPropertyList()+"\",shape=rectangle,color="+color+"];");
       }
       for (StreamEdge edge : edges) {
          out.println("n"+edge.getSource().getId()+" -> n"+ edge.getTail().getId()+ "[label=\"" /*+  " type: " + edge.getType()*/ + " bw: " + edge.getBandwidth() +  "\"] ;");
       }
       out.println("}");

       GraphViz gviz = new GraphViz();
       BufferedImage image = gviz.getGraph(name);

       ImageFrame frame = new ImageFrame(image);
       int height = image.getHeight();
       int width = image.getWidth();
       frame.show(height, width);
   }


   /**
    * This method prints the graph structure
    */
   void print() {
     for (StreamNode node : nodes) {
         System.out.println("Node: " + node.getOp() + " id: " + node.getId() + " in-type: " + node.getInType() + " out-type: " + node.getOutType() + " in-bw: " + node.getInBandwidth() + " out-bw: " + node.getOutBandwidth() + " node: " + node.getSensorNode() );
     }
     for (StreamEdge edge : edges) {
         System.out.println("Edge: " + edge.getSource().getId() + " -> " +  edge.getTail().getId() + " id:" + edge.getId() + " type: " + edge.getType() + " bw: " + edge.getBandwidth() );
     }
   }

   /**
    * This method returns the set of in-coming edges of a node
    * @param node stream node
    * @return A list of in-coming edges to the given node
    */
    List<StreamEdge> getInEdges(StreamNode node) {
      List<StreamEdge> result = new LinkedList<StreamEdge>();

      for(StreamEdge edge : edges) {
          if(edge.getTail() == node) {
              result.add(edge);
          }
      }
      return result;
   }

   /**
    * This method returns the set of out-going edges of a node
    * @param node stream node
    * @return A list of out-going edges
    */
   List<StreamEdge> getOutEdges(StreamNode node) {
      List<StreamEdge> result = new LinkedList<StreamEdge>();

      for(StreamEdge edge : edges) {
          if(edge.getSource() == node) {
              result.add(edge);
          }
      }
      return result;
   }


   /**
    * This method computes the topological sort of the Stream graph nodes
    *
    * @return Array of stream nodes in topological order
    */
   StreamNode[] computeTopologicalOrder() {
      StreamNode result[] = new StreamNode [nodes.size()];
      int nodeCtr=0;
      Set<StreamNode> worklist = new HashSet<StreamNode>();
      Map<StreamNode,Integer> inDegree = new HashMap<StreamNode,Integer>();

      //reset in-degree for all nodes and add sensor nodes to worklist
      for(StreamNode n : nodes){
        inDegree.put(n,0);
        if(n.isSenseOp()){
            worklist.add(n);
        }
      }

       // compute in-degree for other nodes
      try{
        for (StreamEdge edge : edges ) {
            StreamNode v = edge.getTail();
            inDegree.put(v,1+inDegree.get(v));
        }
       }
      catch(Exception e){
          System.out.println("CASCADE: problem with in-degree of nodes in topological sort");
      }

    //while worklist is non-empty do
    while(!worklist.isEmpty()){

      //remove a node n from worklist
      StreamNode n = worklist.iterator().next();
      worklist.remove(n);

      //insert n into sorted result array
      try{
        result[nodeCtr++]= n;
      }
      catch(ArrayIndexOutOfBoundsException e){
          System.out.println("CASCADE: problem in topological sorting of Stream Graph");
      }

     // for successor nodes reduce the in-degree 
      for(StreamEdge edge : getOutEdges(n)){
        StreamNode m = edge.getTail();     
        inDegree.put(m, inDegree.get(m)-1);

        //if m has no other incoming edges then insert m into worklist
        if(inDegree.get(m)==0){
          worklist.add(m);
        }
      }
    }
    //Check to see if all edges are removed
    boolean cycle = false;
    for(StreamNode n : nodes){
      if(inDegree.get(n) != 0){
        cycle = true;
        break;
      }
    }
    if(cycle){
      System.out.println("CASCADE: Cycle present in Stream graph, topological sort not possible");
      return null;
    }else{
      return result;
    }
  }  

   /**
    * This method computes the bandwidth of each edge based on
    * the arrival rate at sensor node and topologically ordered nodes propagating
    * the incoming data
    */
   void computeBandwidth()
   {
       // traverse nodes in topological order
       StreamNode[] topsort = computeTopologicalOrder();
       int n = topsort.length;
       for(int i = 0; i < n; i++){
           StreamNode node = topsort[i];

           // get in- and out-edges of node
           List<StreamEdge> inEdges = getInEdges(node),
                            outEdges = getOutEdges(node);

           // compute input bandwidth
           double in_bw = 0;
           for(StreamEdge edge : inEdges) {
               in_bw = in_bw + edge.getBandwidth();
           }
           node.setInBandwidth(in_bw);


           // compute output bandwidth  
           node.setOutBandwidth(in_bw);
           double out_bw = node.getOutBandwidth();

           // determine weight for outgoing edges
           double weight = 1.0;
           if (outEdges.size() > 1 ) {
               StreamNodeSplit splitter = (StreamNodeSplit) node;
               if(!splitter.isDuplicate()) {
                   weight = 1.0 / (double) outEdges.size();
               }
           }
           
           // compute bandwidth of outgoing edges
           for (StreamEdge edge : outEdges) {
               edge.setBandwidth(weight*out_bw);
           }
       }
   }

   /**
    * This method computes data type of each edge based on topological sort
    * and data type of its incoming edges
    */
   void computeDataType()
   {
        StreamNode[] topsort = computeTopologicalOrder();
       int n = topsort.length;

       for(int i = 0; i < n; i++){
           StreamNode node = topsort[i];
           List<StreamEdge> inEdges = getInEdges(node),
                            outEdges = getOutEdges(node);

           // get input data type and check whether
           // all incoming edges have the same data type
           String in_type = "";
           boolean firstIteration = true;
           for(StreamEdge in_edge: inEdges) {
               if(firstIteration){
        	   firstIteration = false;
        	   in_type = in_edge.getType();
               } else {
                   if (!in_edge.getType().equals(in_type)){
                       System.out.println("CASCADE: Type clash between node " + in_edge.getSource() + " and " + in_edge.getTail());
                       print();
                   }
                }
           }

           if(!inEdges.isEmpty()) {
                node.setInType(in_type);
           }

           // compute output data type of node
           if(!outEdges.isEmpty()) {
                node.setOutType(in_type);
                String out_type = node.getOutType();

                // set data type for outgoing edges
                for (StreamEdge out_edge : outEdges ) {
                    out_edge.setType(out_type);
                }
           }
       }
   }
}
