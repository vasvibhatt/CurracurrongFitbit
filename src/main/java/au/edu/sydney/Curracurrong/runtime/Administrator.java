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

/**
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public abstract class Administrator extends Thread {
    /**
     * This method gets operatorID as a parameter and returns the record of operatorIDs that will
     * send message to this operator
     * @param operatorID of receiver operator
     * @return the record of sender operator IDs
     */
    public abstract String[][] getInputQueues(int operatorID);

    /**
     * This method gets and operatorID as a parameter and returns the record of operatorIDs that will
     * receive message from this operator
     * @param operatorID ID of sender operator
     * @return the record of receiver operator IDs
     */
    public abstract String[][] getOutputputQueues(int operatorID);
}
