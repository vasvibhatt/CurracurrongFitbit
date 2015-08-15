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

package au.edu.sydney.Curracurrong.scheduler;

import au.edu.sydney.Curracurrong.runtime.TimeTriggered;

/**
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public class RunQueue {
    private PriorityBlockingQueue runQueue;

    public RunQueue() {      
         runQueue = new PriorityBlockingQueue(20);
    }

    /**
     * This method adds new task to the run queue for execution
     * @param task The task to be added
     */
    public synchronized void add(TimeTriggered task) {
        runQueue.add(task);
        notifyAll();
    }

    /**
     * This method removes the task from the run queue
     * @param task The task to be removed
     */
    public synchronized void remove(TimeTriggered task) {
        runQueue.remove( task);
        notifyAll();
    } 

    /**
     * This method returns the first task from the run queue to be executed
     * @return operator
     */
    public synchronized TimeTriggered getRootBlock() {
        while (runQueue.isEmpty()) {
            try {
                wait();
            }
            catch (InterruptedException e) {
            }
	}
        TimeTriggered se = (TimeTriggered) runQueue.peek();
        return se;
    }

    /**
     * This method returns the status of run queue if it is empty
     * @return true if the queue is empty; false otherwise
     */
    public boolean isEmpty() {
        boolean result;
        if (runQueue.isEmpty())
            result = true;
        else
            result = false;
        return result;
    }
    
    public int size() {
    	return runQueue.size();
    }

    public boolean contains(TimeTriggered so) {
        return (runQueue.contains(so));
    }
}
