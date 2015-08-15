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

/**
 * This class is used to keep the synchronized time of the SunSPOT, so that a
 * singular global time can be implemented across all the spots.
 *
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
class TimeKeeper {
    private static final TimeKeeper instance = new TimeKeeper();

    private long delta; //the appropriate time delta to keep the spot synchronized to global time

    private TimeKeeper() {
            delta = 0;
    }

    static TimeKeeper getInstance() {
            return instance;
    }

    /**
     * Gets the synchronized time of the spot in milliseconds
     *
     * @return the global system time in milliseconds
     */
    synchronized long getTimeMillis() {
            return System.currentTimeMillis() + delta;
    }
}