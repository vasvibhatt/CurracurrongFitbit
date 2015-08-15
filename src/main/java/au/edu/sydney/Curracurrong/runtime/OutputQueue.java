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

import java.util.Vector;

/**
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public class OutputQueue {

    /**
     * output channel
     */
    private Vector queue;

    /**
     * destination operator id for the channel
     */
    private int receiverOperatorID;

    /**
     * source operator id for the chanel
     */
    private int senderOperatorID;

    /**
     * Unique output channel id
     */
    private int queueID;

    /**
     * Constructor initializes the output channel parameters
     * @param receiverOp destination operator
     * @param senderOp sender operator
     * @param id channel id
     */
    public OutputQueue(int receiverOp, int senderOp, int id) {
        queue = new Vector();
        receiverOperatorID = receiverOp;
        senderOperatorID = senderOp;
        queueID = id;
    }

    /**
     * Add items to the end of vector
     * @param x the item to add
     */
    public void push(Object x) {
        queue.addElement(x);
    }

    /**
     * Removes an item from the head of the vector
     * @return the removed item
     */
    public Object pop() {
        Object temp;
        temp = queue.firstElement();
        queue.removeElementAt(0);
        return temp;
    }

    /**
     * Check is the queue empty or no
     * @return boolean result of isEmpty
     */
    public boolean isEmpty() {
        if (queue.isEmpty() == true) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get the number of elements in the queue
     * @return number of elements in the queue
     */
    public int size() {
        return queue.size();
    }

    /**
     * Get the receiver operator ID to this queue
     * @return receiver operator ID
     */
    public int getReceiverOperatorID() {
        return receiverOperatorID;
    }

    /**
     * Get the operator ID which sends data to this queue
     * @return sender operator ID
     */
    public int getSenderOperatorID() {
        return senderOperatorID;
    }

    /**
     * Get the queue ID of the queue
     * @return queue ID
     */
    public int getQueueID() {
        return queueID;
    }
}
