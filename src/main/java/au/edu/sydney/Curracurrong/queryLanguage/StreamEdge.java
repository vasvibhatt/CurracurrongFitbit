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
public class StreamEdge {
    /**
     * Source node of the edge
     */
   protected StreamNode source;
   /**
    * Tail node of the edge
    */
   protected StreamNode tail;
   /**
    * Unique edge id
    */
   protected int id;
   /**
    * Bandwidth of the edge
    */
   protected double bandwidth;
   /**
    * Type of the data propagated on the edge
    */
   protected String type;

   StreamEdge(StreamNode src, StreamNode dest) {
      source = src;
      tail = dest;
   }

   /**
    * This method returns the source node of the edge
    * @return source node
    */
   StreamNode getSource() {
     return source;
   }

   /**
    * This method returns the tail node of the edge
    * @return tail node
    */
   StreamNode getTail() {
     return tail;
   }

   /**
    * This method sets the unique id to the edge
    * @param id Unique edge id
    */
   void setId(int id) {
     this.id =  id;
   }

   /**
    * This method returns the unique edge id
    * @return Edge id
    */
   int getId() {
     return id;
   }

   /**
    * This method sets the bandwidth for the edge
    * @param bwd Bandwidth/capacity
    */
   void setBandwidth(double bwd) {
       bandwidth = bwd;
   }

   /**
    * This method returns the bandwidth of the edge
    * @return Bandwidth/capacity
    */
   double getBandwidth() {
       return bandwidth;
   }

   /**
    * This method sets the data type for the edge
    * @param datatype Type of propagating data
    */
   void setType(String datatype) {
       type = datatype;
   }

   /**
    * This method returns the data type for the edge
    * @return Type of propagating data
    */
   String getType() {
       return type;
   }
}
