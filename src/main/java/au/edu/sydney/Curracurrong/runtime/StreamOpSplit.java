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

package au.edu.sydney.Curracurrong.runtime;

import au.edu.sydney.Curracurrong.datatype.Data;
import java.util.Hashtable;

/**
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public class StreamOpSplit extends StreamOperator {

    int currentChannel = 0;
    boolean copySemantics = false;
    
    /**
     * This method initializes Spit operator parameters
     * @param opID Operator Id
     */
    public void initialize(int opID, Hashtable propertyMap) {
      // Check for copy semantics
        super.initialize(opID,propertyMap);
      
        Data value = getPropertyValue("type");
        if(value != null) {
             if(value.tostring().compareTo("duplicate") == 0) {
                 copySemantics = true;
             }
        }
    }

    /**
     * This method executes the Split operator
     */
    public synchronized void execute() {
        // split the data to the number of output queues
        if(isDataAvailable(0, 0)) {
            Object element = receive(0);
            if (copySemantics) {
                for (int channel = 0; channel < getNumOutputChannels(); channel++) {
                    send(channel,element);
                }
            } else {
                send(currentChannel,element);
                currentChannel = ((currentChannel + 1) % getNumOutputChannels());
            }
        }
    }   
}