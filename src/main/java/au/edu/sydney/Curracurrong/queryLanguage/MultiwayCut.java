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
public class MultiwayCut {

    private class MultiwayCutEdge {
    /**
     * The node representing a source node of the edge.
     */
    int source;
    /**
     * The node representing a tail node of the edge.
     */
    int tail;
    /**
     * The node representing a flow capacity of the edge.
     */
    int capacity;

    /**
     * This constructor initializes the edge with source node, tail node and capacity
     * <p>
     * @param sNode The source node of the edge
     * @param tNode The tail node of the edge
     * @param cap The flow capacity of the edge
     */

        MultiwayCutEdge(int source,int tail, int capacity) {
            this.capacity = capacity;
            this.source = source;
            this.tail = tail;
        }

        /**
         * This method returns the source node of the edge
         * @return source node
         */
        int getSource() {
            return source;
        }

        /**
         * This method returns the tail node of the edge
         * @return tail node
         */
        int getTail() {
            return tail;
        }

        /**
         * This method returns the flow capacity of the edge
         * @return capacity
         */
        int getCapacity() {
            return capacity;
        }
    }

    /**
     * This represents the number of nodes in the stream graph.
     * <p>
     * In stream graph nodes are assigned numbers between 0 and (numNodes - 1).
     * There are two types of nodes
     * <ul>
     * <li> Terminals
     * <li> Non-terminals
     * </ul>
     */
    int numNodes;
    /**
     * This represents the number of non-terminal nodes in the stream graph.
     * <p>
     * 0 <= i < numNonTerminals
     */
    int numNonTerminals;
    /**
     * This represents the number of terminal nodes in the stream graph.
     * <p>
     * numNonTerminals <= i < numNodes
     */
    int numTerminals;

    /**
     * This represents the edges in the Multiway graph
     *
     */
    List<MultiwayCutEdge> edges;
    /**
     * This represents the partitioning of nodes.
     * <p>
     * Each node in Multiway graph gets assigned a particular terminal node and creates partitions.
     */
    int [] nodePartitioning;
    int [] terminalNode;


    // we assume that first we have non-terminals and then terminals
    /**
     * Constructor initializes total number of nodes and terminal nodes.
     * It is considered that node list starts with all non-terminal nodes and
     * ends with all terminal nodes.
     * @param nodes Total number of nodes in Multiway graph
     * @param terminals Total number of terminal nodes in Multiway graph
     */
    public MultiwayCut(int numNodes, int numTerminals, int terminalNodes[]) {
        assert numNodes > 0;
        assert numTerminals > 1&& numTerminals <= numNodes;

        // initialize member variables
        this.numNodes = numNodes;
        this.numTerminals = numTerminals;
        this.numNonTerminals = (numNodes - numTerminals);
        this.terminalNode = terminalNodes;
        edges = new LinkedList<MultiwayCutEdge>();

        // set all nodes to undefined partition
        nodePartitioning = new int[numNodes];
        for(int node = 0; node < numNodes; node++) {
           nodePartitioning[node] = terminalNodes[numTerminals-1];
        }
        // set terminal nodes to their own partition
        for(int terminal : terminalNodes){
           nodePartitioning[terminal] = terminal;
        }
    }

    public Boolean isTerminal(int node){
        for(int u=0; u<numTerminals; u++){
            if(terminalNode[u] == node){
                return true;
            }
        }
        return false;
    }
    /**
     * This method adds a new edge to the multiway graph
     * @param source The source node of an edge
     * @param tail The tail node of an edge
     * @param capacity The flow capacity of an edge
     */

    void addEdge(int source, int tail, int capacity) {
        assert 0 <= source && source < numNodes;
        assert 0 <= tail && tail < numNodes;
        edges.add(new MultiwayCutEdge(source,tail,capacity));
    }

    /**
     * Compute multiway cut by executing ST-min cut with number of terminals
     * each time one terminal is source and all other terminals act as one sink node
     * <p>
     * assertion: first non terminals then terminals and sink at the end
     */
    void compute() {
       int sSTMin, tSTMin;

        // mapping non-terminal nodes of stream graph to multiway graph
       Map<Integer,Integer> mapstreamMC = new HashMap<Integer, Integer>();
       int nonTerminalCounter = 0;

       for(int nodes=0; nodes < numNodes; nodes++){
           if(!isTerminal(nodes)) {
                mapstreamMC.put(nodes, nonTerminalCounter++);
            }
        }

        // construct st min cut problem for all terminal
        // vertices except the last terminal vertex.
        for(int terminal = 0; terminal < numTerminals; terminal++) {           
            int separatingNode = terminalNode[terminal];    

            int numSTMinCutNodes = numNonTerminals + 2;
            sSTMin = numNodes;
            tSTMin = numNodes + 1;

            STMinCut problem = new STMinCut(numSTMinCutNodes);

            for(int u=0;u < numNodes; u++) {
                if(!isTerminal(u)) {
                    problem.addNode(u);
                }
            }
            problem.addNode(sSTMin);  // s node
            problem.setStart(sSTMin);

            problem.addNode(tSTMin); // t node
            problem.setSink(tSTMin);

            // construct graph for st-min cut by assigning one terminal at source and other
            // terminals and sink as one sink node
            for (MultiwayCutEdge edge: edges) {
                int uSTMin;
                int vSTMin;
                int srcMC = edge.getSource();
                int destMC = edge.getTail();

                // map a node either to the same node in the st mincut problem
                // or to s if it is the current terminal vertex
                // or to t if its any other terminal vertex.

                if(isTerminal(srcMC)) {
                    if (srcMC == separatingNode) {
                        uSTMin = sSTMin;
                    } else {
                        uSTMin = tSTMin;
                    }
                } else {
                    uSTMin = srcMC;
                }

                if(isTerminal(destMC)) {
                    if (destMC == separatingNode) {
                        vSTMin = sSTMin;
                    } else {
                        vSTMin = tSTMin;
                    }
                } else {
                    vSTMin = destMC;
                }

                problem.addSTMinCutEdge(uSTMin, vSTMin, edge.getCapacity());
            }

            // compute s-t min cut
            problem.compute();

            // add all vertices of the S partition
            // to the partition of the current terminal
            // vertex
            for (int u = 0; u < numNodes; u++) {
                if (problem.findNode(u) != -1){
                   if(problem.inSPartition(u)){
                      nodePartitioning[u] = separatingNode;
                   }
                }
            }
        }
    }

    /**
     * This method returns the partition for a given node
     * @param node A node in the Multiway graph
     * @return The partition number for a given node <code>u</code>
     */
    int getPartition(int node) {
        assert 0 <= node && node < numNodes;
        assert 0 <= nodePartitioning[node] && nodePartitioning[node] <= numNodes;
        return nodePartitioning[node];
    }
 
}
