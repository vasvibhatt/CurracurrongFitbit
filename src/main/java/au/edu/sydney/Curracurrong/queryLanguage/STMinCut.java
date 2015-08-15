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
public class STMinCut {
    
    private class STMinCutEdge {

        /**
         * This represents the start/source node of an edge
         */
        private final int fromNode;
        /**
         * This represents the end/tail node of an edge
         */
        private final int toNode;
        /**
         * This represents the flow capacity of an edge
         */
        private final int capacity;
        /**
         * This represents the current maximum flow of an edge
         */
        private int flow;             

        /**
         * Constructor initializes the edge parameters
         * @param v The start node of an edge
         * @param w The end node of an edge
         * @param cap The flow capacity of an edge
         */
        public STMinCutEdge(int fromNode, int toNode, int capacity) {
            this.fromNode = fromNode;
            this.toNode = toNode;
            this.capacity  = capacity;
            flow = 0;
        }

        /**
         Constructor initializes the edge parameters
         * @param fNode  The start node of an edge
         * @param tNode  The end node of an edge
         * @param cap The flow capacity of an edge
         * @param fl The flow of an edge
         */
        public STMinCutEdge(int fromNode, int toNode, int capacity, int flow) {
            this.fromNode = fromNode;
            this.toNode = toNode;
            this.capacity  = capacity;
            this.flow = flow;
        }

       
        /**
         * This method returns the start node of an edge
         * @return fromNode of an edge
         */
        public int from() {
            return fromNode;
        }

        /**
         * This method returns the end node of an edge
         * @return toNode of an edge
         */
        public int to() {
            return toNode;
        }

        /**
         * This method returns the flow capacity of an edge
         * @return capacity of an edge
         */
        public int capacity()  {
            return capacity;
        }

        /**
         * This method returns the actual maximum flow of an edge
         * @return flow of an edge
         */
        public int flow() {
            return flow;
        }

        /**
         * This method returns the another end node of an edge for a given node
         * If given node is a fromNode of an edge then method will return toNode of the edge else vice versa
         * @param node  Node of an edge
         * @return  Another end node of an edge
         */
        public int other(int node) {
            assert (node == fromNode) || (node == toNode);
            if (node == fromNode) {
                return toNode;
            } else {
                return fromNode;
            }            
        }

        /**
         * This method returns the residual capacity of an edge
         * @param vertex The node of an edge
         * @return The residual capacity  of an edge for given vertex
         */
         
        public int residualCapacityTo(int node) {
            assert (node == fromNode) || (node == toNode);
            if (node == fromNode) {
                return flow;
            } else {
                return capacity - flow;
            }            
        }

        /**
         * This method adds the residual flow to an edge
         * @param vertex The node of an edge
         * @param delta  The residual flow to be added to an edge for a given vertex
         */
         
        public void addResidualFlowTo(int vertex, int delta) {
            assert (vertex == fromNode) || (vertex == toNode);
            if (vertex == fromNode) {
                flow -= delta;
            } else {
                flow += delta;
            }            
        }
    }

    /**
     * List stores the adjacent edges for a given node
     */
    private List<STMinCutEdge> [] adj;
    /**
     * Mark true if there is a path s->v in residual graph
     */
    private boolean [] marked;
    /**
     * List of all the edges that has the given toNode
     */
    private STMinCutEdge[] edgeTo;
    /**
     * Current value of max flow
     */
    private int value;
    /**
     * Number of nodes in residual graph
     */
    private int numNodes;
    /**
     * Source node for ST Min-cut problem
     */
    private int sSTMin;
    /**
     * Destination node for ST Min-cut problem
     */
    private int tSTMin;

    /**
     * Nodes
     */
    private Map<Integer, Integer> nodes;

    /**
     *
     * @param nodes Total number of nodes in ST Min-cut problem
     * @param sSTNode  Source node
     * @param tSTNode  Destination node
     */
    public STMinCut (int nodes) {
        assert nodes > 0;
        /*assert 0 <= sSTNode && sSTNode < nodes;
        assert 0 <= tSTNode && tSTNode < nodes;*/

        //this.numNodes = nodes;
        this.nodes = new HashMap<Integer,Integer>();
        this.adj = new LinkedList[nodes];

        for(int node = 0; node < nodes; node++) {
            adj[node] = new LinkedList<STMinCutEdge>();
        }
        /*this.sSTMin = sSTNode;
        this.tSTMin = tSTNode;*/
    }

    /**
     * set S node for ST Min-cut 
     */
    public void setStart(int startNode){
        assert 0 <= startNode && startNode < numNodes;
        this.sSTMin = findNode(startNode);
    }
    
    /**
     * set T node for ST Min-cut 
     */
    public void setSink(int sinkNode){
        assert 0 <= sinkNode && sinkNode < numNodes;
        this.tSTMin = findNode(sinkNode);
    }

    /**
     * add node to ST problem
     */
    public void addNode(int label) {
        nodes.put(numNodes++, label);
    }

    /**
     *
     */
    public int findNode(int label){
        for(int u=0; u<numNodes; u++){
            if(nodes.get(u) == label) {
                return u;
            }
        }        
        return -1;
    }

    /**
     * This method computes the max flow in a graph
     * and also computes cut vertices
     */
    public void compute(){
        value = excess(tSTMin);
        assert (isFeasible(numNodes, sSTMin, tSTMin));
      

        // while there exists an augmenting path, use it
        while (hasAugmentingPath(numNodes, sSTMin, tSTMin)) {

            // compute bottleneck capacity
            int bottle = Integer.MAX_VALUE;
            for (int v = tSTMin; v != sSTMin; v = edgeTo[v].other(v)) {
                bottle = Math.min(bottle, edgeTo[v].residualCapacityTo(v));
            }
            // augment flow
            for (int v = tSTMin; v != sSTMin; v = edgeTo[v].other(v)) {
                edgeTo[v].addResidualFlowTo(v, bottle);
            }
            value += bottle;
        }

        // check optimality conditions
        //assert check(numNodes, sSTMin, tSTMin);
    }

    /**
     * This method return the max flow value
     * @return Current max flow value
     */
    public int getValue() {
        return value;
    }

    /**
     * This method returns true if the given node is in S partition
     * @param node The node in a ST min-cut problem
     * @return True if given node is in S partition else false
     */
    public boolean inSPartition(int node) {
        return marked[findNode(node)];
    }

    /**
     * Check if augmenting path exists, otherwise return null
     * @param nodes Number of nodes in Multiway Graph
     * @param sNode  from node
     * @param tNode  to node
     * @return True if augmenting path exists
     */

    private boolean hasAugmentingPath(int nodes, int sNode, int tNode) {
        edgeTo = new STMinCutEdge[nodes];
        marked = new boolean[nodes];

        // breadth-first search
        Queue<Integer> q = new LinkedList<Integer>();
        q.add(sNode);
        marked[sNode] = true;
        while (!q.isEmpty()) {
            int v = q.remove();

            for (STMinCutEdge e : adjEdge(v)) {
                int w = e.other(v);

                // if residual capacity from v to w
                if (e.residualCapacityTo(w) > 0) {
                    if (!marked[w]) {
                        edgeTo[w] = e;
                        marked[w] = true;
                        q.add(w);
                    }
                }
            }
        }

        // is there an augmenting path?
        return marked[tNode];
    }


    /**
     * This method returns the excess flow at node v
     * @param node node in residual graph
     * @return excess flow value for an edge with given node v
     */
    private int excess(int node) {
        int excess = 0;
        for (STMinCutEdge e : adjEdge(node)) {
            if (node == e.from()) {
                excess -= e.flow();
            } else {
                excess += e.flow();
            }
        }
        return excess;
    }

    /**
     * This method returns true if feasible path exists in residual graph
     * @param nodes Total nodes in graph
     * @param sNode Source node
     * @param tNode Destination node
     * @return True if path exists between s and t
     */
    private boolean isFeasible(int nodes, int sNode, int tNode) {
        int delta = Integer.MAX_VALUE;

        // check that capacity constraints are satisfied
        for (int v = 0; v < nodes; v++) {
            for (STMinCutEdge e : adjEdge(v)) {
                if (e.flow() < 0 || e.flow() > e.capacity()) {
                    System.err.println("Edge does not satisfy capacity constraints: " + e);
                    return false;
                }
            }
        }

        // check that net flow into a vertex equals zero, except at source and sink
        if ((value + excess(sNode)) > delta) {
            return false;
        }
        if (Math.abs(value - excess(tNode)) > delta) {
            return false;
        }
        for (int v = 0; v < nodes; v++) {
            if (v == sNode || v == tNode) continue;
            else if (Math.abs(excess(v)) > delta) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method adds new Edge from Multiway Cut graph to ST problem
     * @param source source node for new edge
     * @param tail tail node for new edge
     * @capacity flow capacity for new edge
     */
    public void addSTMinCutEdge(int source, int tail, int capacity) {
       source = findNode(source);
       tail = findNode(tail);
       assert 0 <= source && source < numNodes;
       assert 0 <= tail && tail < numNodes;
       assert capacity > 0;

       STMinCutEdge newEdge = new STMinCutEdge(source, tail, capacity);
       adj[source].add(newEdge);
       adj[tail].add(newEdge);
    }

    /**
     * This method returns all adjacent edges of a given node
     * @param node The node in residual graph
     * @return List of edges those are adjacent to given node
     */
    public List<STMinCutEdge> adjEdge(int node) {
        return adj[node];
    }
}
