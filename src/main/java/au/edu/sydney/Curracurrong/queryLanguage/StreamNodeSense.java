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

import au.edu.sydney.Curracurrong.datatype.DInteger64;
import java.util.*;

public class StreamNodeSense extends StreamNode implements Constants {

    @Override
    boolean isSenseOp() {
       return true;
    }

    @Override
    void setOutBandwidth(double inputBandwidth) {
        double rate = DEFAULT_SAMPLE_RATE;
        if (propertyList.containsKey("rate")) {
            rate = (Double)propertyList.get("rate");
        }
        //outBandwidth = rate * 5 * 4 ;
        outBandwidth = rate * 15 * 8 * 8;
    }

    @Override
    void setOutType(String inputType){
        
    }

   @Override
   void setPropertyList(Map<String,Object> plist){
       propertyList = plist;
       init();
   }


    @Override
    void init() {
       super.init(); 
       if (!propertyList.containsKey("node")) {
          System.out.println("CASCADE: Sensor node is missing in sense operation " + getId());
       }
       outType = "R[DInteger64,DInteger64,DFloat,DInteger64,DInteger64,DInteger64,DInteger64]";//,DInteger64,DInteger64,DInteger64,DInteger64,DInteger64,DInteger64,DInteger64,DInteger64,DInteger64]";
    }   
}
