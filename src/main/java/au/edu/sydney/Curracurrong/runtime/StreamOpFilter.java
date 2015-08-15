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
abstract public class StreamOpFilter extends StreamOperator {

   /**
     * This method checks for the availability of the specified data in input channel
     * @param item index of the data item
     * @return true if data available, false otherwise
     */
    protected boolean isDataAvailable(int item) {
        return isDataAvailable(0,item);
    }

    /**
     * This method returns the top element of the channel and removes it.
     * @return data
     */
    protected Object receive(){
        return receive(0);
    }

   /**
     * This method returns the n-th element of the channel (but does not remove it).
     * @param item index of the data item
     * @return data
     */
    protected Object peek(int item){
        return peek(0,item);
    }

    /**
     * This method sends the data to next operator with specified output channel
     * @param item data
     */
    protected void send(Object item){
        send(0,item);
    }
}