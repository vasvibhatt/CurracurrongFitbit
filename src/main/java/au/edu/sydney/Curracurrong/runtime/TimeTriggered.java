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
public abstract class TimeTriggered extends StreamOperator {

    /**
     * Start time for operator execution
     */
    private static long startTime;

    /**
     * Repetition factor for operator execution
     */
    private static int repetition;

    /**
     * Time interval between each repeated execution
     */
    private static long timeInterval;

    private static long commDelay;
    
    private static long timeOut;

    protected TimeTriggered() {        
    }

    /**
     * This method initializes the Time-triggered operator
     * @param start start time
     * @param repeat repetition factor
     * @param interval time interval
     */
    public static void initialize(long start, int repeat, long interval, long timeout) {
    	if(start == 0) {
    		start = System.currentTimeMillis();
    	} else {
    		startTime = start;
    	}
        repetition = repeat;
    	//repetition = 10;
        timeInterval = interval;
        
        timeOut = startTime + timeout;
    }

    /**
     * This method returns the time interval
     * @return time interval
     */
    public long getTimeInterval() {
        return timeInterval;
    }

    /**
     * This method returns the repetition factor
     * @return repetition factor
     */
    public int getRepetition() {
        return repetition;
    }
    
    
    public long getTimeOut() {
    	return timeOut;
    }

    /**
     * This method returns the start time
     * @return start time
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * This method is used to set the next start time for the operator
     * @param time start time
     */
    public void setStartTime(long time){
        startTime = time;
    }

    /**
     * This method is used to set the repetition factor
     * @param repeat repetition
     */
    public void setRepeat(int repeat){
        repetition = repeat;
    }

    /**
     * This method is used to set the time interval
     * @param interval time interval
     */
    public void setInterval(long interval) {
        timeInterval = interval;
    }

    
    public static void setCommDelay(long delay) {
        System.out.println("SEtting commdelay...");
        commDelay = delay;
    }

    public static long getCommDelay() {
        return commDelay;
    }
    
    public void setTimeOut(long timeout) {
    	timeOut = timeout;
    }
}
