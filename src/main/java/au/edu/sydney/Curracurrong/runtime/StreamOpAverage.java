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

import au.edu.sydney.Curracurrong.datatype.DFloat;
import au.edu.sydney.Curracurrong.datatype.DInteger32;
import au.edu.sydney.Curracurrong.datatype.DRecord;
import au.edu.sydney.Curracurrong.datatype.DString;
import au.edu.sydney.Curracurrong.datatype.Data;
import au.edu.sydney.Curracurrong.datatype.ParsingException;
import java.util.Hashtable;

/**
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public class StreamOpAverage extends StreamOpFilter {

    /**
     * number of data elements for average
     */
    private int windowSz;

    /**
     * Initializes the stream operator
     * @param opID operator id
     * @param propertyMap property list
     */
    public void initialize(int opID, Hashtable propertyMap) {
        // call constructor of super-class to initialize
        // channels
        super.initialize(opID, propertyMap);

        Data value = getPropertyValue("window");
        if(value != null) {
             windowSz = Integer.parseInt(value.tostring());
        }
    }

    /**
     * executes the stream operator
     */
    public synchronized void execute() {
        if (isDataAvailable(windowSz)) {
            float sum = 0;
            DRecord rec = null;
            for (int idx = 0; idx < windowSz; idx++) {
                rec = (DRecord) peek(idx);
                try {
                float val = Float.parseFloat((rec.getElement(6)).tostring());
                sum = sum + val;
                } catch(ParsingException pe){
                    System.out.println("Cascade: parsing exception in Average filter");
                }
               
            }
            DFloat average = new DFloat(sum / windowSz);
            for(int idx = 0; idx < windowSz; idx++) {
                receive();
            }
            try{
                rec.setElement(6, average);
            } catch(ParsingException pe){
                System.out.println("Cascade: parsing exception in Average filter");
            }
            send(rec); // channel id
        }
    }
}