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
public class StreamNodeSplit extends StreamNode {
    StreamNodeSplit() {
      super();
      setSensorNode("0.0.0.0");
    }

    @Override
    boolean isSenseOp() {
        return false;
    }

    /**
     * This method decides the type of Splitter: Duplicate or Round-robin
     * @return True if Splitter type is Duplicate, false otherwise
     */
    boolean isDuplicate() {
        if (propertyList.containsKey("type")) {
            return ((String)propertyList.get("type")).equals("duplicate");
        } else {
            return false;
        }
    }
    
    @Override
    void setOutBandwidth(double inputBandwidth) {
        outBandwidth = inputBandwidth; 
    }

    @Override
    void setOutType(String inputType){
        outType = inputType;
    }

    @Override
    void init() {
       super.init();
        if (!propertyList.containsKey("node")) {
          System.out.println("CASCADE: Sensor node is missing in sense operation " + getId());
       }
       if (!propertyList.containsKey("type")) {
          System.out.println("CASCADE: type parameter is missing in split operation " + getId());
       }
    }
    
}

