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

/**
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public final class DisjointSets {
    /**
     * Array of parent nodes
     */
    int parent[];
    /**
     * Array of ranks
     */
    int rank[];
    /**
     * Total number of elements in disjoint sets
     */
    int numElements;
    /**
     * Array of partition
     */
    int partition[];
    /**
     * Number of disjoint sets
     */
    int numDisjointSets;

    /**
     * Constructor
     * @param numElements number of elements to be allocated in disjoint sets
     */
    public DisjointSets(int numElements) {
        this.numElements = numElements;
        this.parent = new int[numElements];
        this.rank = new int[numElements];
        this.partition = new int[numElements];
        this.numDisjointSets = 0;
        init();
    }
    /**
     * Initializes disjoint set parameters
     */
    private void init(){
       for(int idx=0;idx < numElements; idx++){
          parent[idx] = idx;
          rank[idx] =0;
        }
    }

    /**
     * Make a singleton for element with index idx
     * @param idx
     */
    public void makeSet(int idx){
       assert 0<= idx && idx < numElements;
       parent[idx]=idx;
    }


    /**
     * Find a set
     * @param idx index
     */
    public int find(int idx){
       assert 0<= idx && idx < numElements;
        while(parent[idx] != idx){
            idx = parent[idx];
        }
        return idx;
    }

    /**
     *  Assign ordinal number to disjoint sets
     *  between 0 and number of disjoint sets
     *  in the partitioning. We refer to the
     *  ordinal number of a disjoint set as a
     *  partition (number).
     */
    public void computePartitionNumbers() {
        numDisjointSets = 0;
        for(int idx=0; idx < numElements; idx++) {
            if (find(idx) == idx) {
                partition[idx] = numDisjointSets++;
            }
        }
        
        for(int idx=0; idx < numElements; idx++) {
            if (find(idx) != idx) {
               partition[idx] = partition[find(idx)];
            }
        }        
    }

    /** 
     * gets a number of disjoint sets
     * @return number of disjoint sets
     */
    public int getNumDisjointSets(){
        return numDisjointSets;
    }
    
    /**
     * Gets partition number of a disjoint set / node
     * @return partition number
     */
    public int getPartitionNumber(int idx){
        assert 0 <= idx && idx < numElements;
        return partition[idx];           
    }

    /**
     * Get leader
     * @param partitionNumber partition
     * @return leader node
     */
    public int getLeader(int partitionNumber){
       assert 0 <= partitionNumber && partitionNumber < numDisjointSets;
       for(int idx=0;idx< numElements;idx++){
           if (parent[idx] == idx && partition[idx] == partitionNumber ) {
                 return idx;
           }
        }
       assert true;
       return -1;
    }

    /**
     * Link two root nodes of two sub-trees
     * @param x node
     * @param y node
     */
    public void link(int x,int y){
       assert 0<= x && x < numElements;
       assert 0<= y && y < numElements;

       if (rank[x] > rank[y]) {
          parent[y] = x;
        } else {
          parent[x] = y;
          if (rank[x] == rank[y]) {
              rank[y]++;
          }
       }
    }


    /**
     * union of two root nodes
     * @param x node
     * @param y node
     */
    public void union (int x, int y){
      assert 0<= x && x < numElements;
      assert 0<= y && y < numElements;
      
      link(find(x),find(y));
    }
}
