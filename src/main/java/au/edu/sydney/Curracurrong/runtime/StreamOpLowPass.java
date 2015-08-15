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
public class StreamOpLowPass extends StreamOpFilter {

    /**
     * number of data elements for average
     */
    private int orderNum;
    private float alpha; 
    private float[] y;
    private int windowSz;

    /**
     * Initializes the stream operator
     * @param opID operator id
     * @param propertyMap property list
     */
    public void initialize(int opID, Hashtable propertyMap) {

       // initialize
       super.initialize(opID,propertyMap);


       // filter operator specific section
       Data order = getPropertyValue("order");
        if(order != null) {
             orderNum = Integer.parseInt(order.tostring());
        } else {
           orderNum = 3;
        }

       // cut off frequency for low-pass filter
       float cutOffFreq;
       Data freqValue = getPropertyValue("freq");
        if(freqValue != null) {
             cutOffFreq = Float.parseFloat(freqValue.tostring());
        } else {
           cutOffFreq = 10;
        }

       // create filter
       y = new float[orderNum];

       float dT;
       float pi = 3.14f;
   
       Data dataValue = getPropertyValue("dt");
        if(dataValue != null) {
             dT = Float.parseFloat(dataValue.tostring());
        } else {
           dT = 1;
        }

       alpha = dT / (2 * pi * cutOffFreq + dT);
    }

    /**
     * executes the stream operator
     */
    public synchronized void execute() {
        if (isDataAvailable(windowSz)) {

            // compute new value
            float sum = 0;
            for (int idx = 0; idx < windowSz; idx++) {
                float val = Float.parseFloat(((DFloat) peek(idx)).tostring());
                sum = sum + alpha * val + (1-alpha) * y[idx];
            }
            DFloat out = new DFloat(sum);

            // update output rate 
            for (int idx = windowSz-1; idx>0; idx--) {
                y[windowSz-1] = y[windowSz];
            }
            y[windowSz-1] = sum;
            
            receive();
            send(out);
        }
    }
}
