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

public abstract class StreamNode {
    /**
     * Unique node id
     */
   protected int id;

   /**
    * List of properties for a stream node
    */
   protected Map<String,Object> propertyList;

   /**
    * In-coming data type for a node
    */
   protected String inType;

   /**
    * Out-going data type for a node
    */
   protected String outType;

   /**
    * In-coming data bandwidth for a node
    */
   protected double inBandwidth;

   /**
    * Out-going data bandwidth for a node
    */
   protected double outBandwidth;

   /**
    * Sensor node to which node is allocated after Multiway Cut
    */
   protected String sensorNode;

   /**
    * Constructor
    */
   StreamNode(){
      id = -1;
      propertyList = new HashMap<String,Object>();
      sensorNode = "";
   }

   /**
    * This method sets the id to a node
    * @param id Unique node id
    */
   void setId(int id){
       this.id = id;
   }

   /**
    * This method returns the node id
    * @return Unique node id
    */
   int getId() {
      return id;
   }

   /**
    * This method returns the type of the operator a node represents
    * @return Operator name
    */
   String getOp() {
      return this.getClass().getName();
   }

    /**
    * This method sets the sensor to a node selected after Multiway Cut
    * @param s Sensor node
    */
   void setSensorNode(String s) {
       sensorNode = s;
   }

   /**
    * This method returns the sensor to which the node is allocated
    * @return Sensor node
    */
   String getSensorNode() {
       return sensorNode;
   }

   /**
    * This method returns true if the node represents a sensor
    * @return True if node is sensor, false otherwise
    */
   abstract boolean isSenseOp();

  
   /**
    * This method sets the property list to a node
    * @param plist Property list for a node
    */
   void setPropertyList(Map<String,Object> plist){
       propertyList = plist;
       init();
   }

    /**
    * This method returns the list of property for a node
    * @return list of property
    */
    Map<String,Object> getPropertyList() {
       return propertyList;
   }

   /**
    * This method converts the property list to more readable String format
    * @return Property list in String format
    */
   String toStringPropertyList() {
     String text = "";
     for (Map.Entry<String,Object> entry: propertyList.entrySet())  {
        text = text +  entry.getKey()+"="+ entry.getValue() + " "; 
     }
     if (text.length()>0){
        text = "[" + text.substring(0,text.length()-1) + "]";
     }
     return text;
   } 

   /**
    * This method computes and sets the output bandwidth of a node
    * @param inputBandwidth Input data bandwidth/capacity
    */
   void setOutBandwidth(double inputBandwidth) {
        outBandwidth =  inputBandwidth;
   }

   /**
    * This method returns the output bandwidth of a node
    * @return output bandwidth/capacity
    */
   double getOutBandwidth() {
      return outBandwidth;
   }
   
   /**
    * This method sets the input data bandwidth for a node
    * @param bw Bandwidth/Capacity
    */
   void setInBandwidth(double bw) {
      inBandwidth = bw;
   }

   /**
    * This method returns the input bandwidth of a node
    * @return Input data bandwidth/capacity
    */
   double getInBandwidth() {
      return inBandwidth;
   }


    /**
   * This method computes the output data type for a node
   * @param inputType Input data type
   */
    void setOutType(String inputType) {
        outType = new String(inputType);
    }

    /**
    * This method returns the output data type of a node
    * @return Output data type
    */
   String getOutType() {
      return outType;
   }

    /**
    * This method sets the input data type for a node
    * @param type Input data type
    */
   void setInType(String type) {
      inType = new String(type);
   }

   /**
    * This method returns the input data type for a node
    * @return Input data type
    */
   String getInType() {
      return inType;
   }
   
   /**
    * 
    * This method initializes the node with property list
    */
   void init() {
       if (propertyList.containsKey("node")) {
          setSensorNode(propertyList.get("node").toString());
       }  
   } 
}
