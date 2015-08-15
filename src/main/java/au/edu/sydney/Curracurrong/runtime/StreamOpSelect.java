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

import au.edu.sydney.Curracurrong.datatype.Composite;
import au.edu.sydney.Curracurrong.datatype.DInteger32;
import au.edu.sydney.Curracurrong.datatype.DRecord;
import au.edu.sydney.Curracurrong.datatype.DString;
import au.edu.sydney.Curracurrong.datatype.Data;
import au.edu.sydney.Curracurrong.datatype.ParsingException;
import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public class StreamOpSelect extends StreamOpFilter {

    /**
     * list of field numbers those are to be selected from the incoming data
     */
    private int field[];

    /**
     * Initializes the stream operator
     * @param opID operator id
     * @param propertyMap property list
     */
    public void initialize(int opID, Hashtable propertyMap) {
       super.initialize(opID,propertyMap);

       //extract properties from property map
       String value = getPropertyValue("field").tostring();
       if(value != null) {
           StringTokenizer fieldValue = new StringTokenizer(value, ",");
           field = new int[fieldValue.countTokens()];
           int idx = 0;
           while(fieldValue.hasMoreTokens()){
               field[idx] = Integer.parseInt(fieldValue.nextToken());
               idx++;
           }
       }
    }
    
    Data getPropertyValue(String propertyKey) {
        Data value = null;
        // filter operator specific section
        if (propertyMap.containsKey(propertyKey)) {
            DRecord elem = (DRecord) propertyMap.get(propertyKey);
            try {
                DString readValue = (DString) elem.getElement(1);
                value = new DString(readValue.tostring());
            } catch (ParsingException ex) {
                System.out.println(ex);
            }
        }
        return value;
    }

    /**
     * executes the stream operator
     */
    public void execute() {
        Composite inData;
        //System.out.println("About to execute Select op");
        DRecord outData = new DRecord(4+field.length);
        if(isDataAvailable(0)) {
            inData = (Composite) receive();
            try {
                for(int idx=0; idx<4; idx++) {
                    ((DRecord)outData).setElement(idx, inData.getElement(idx));
                }
            
                if(field.length > 1) {
                    for(int idx=0; idx<field.length; idx++) {
                        if(inData.getElement(field[idx]) != null) {
                            ((DRecord)outData).setElement(4+idx, inData.getElement(4+field[idx]-1));
                        }
                    }
                } else {
                    outData.setElement(4, inData.getElement(4+field[0]-1));
                }
                send(outData);
            } catch(ParsingException pe) {
                System.out.println("CASCADE: parsing failed in StreamNodeSelect execute");
            } catch(Exception e) {
                System.out.println("CASCADE: failed during execution in StreamNodeSelect");
                e.printStackTrace();
            }
        }
    }
}