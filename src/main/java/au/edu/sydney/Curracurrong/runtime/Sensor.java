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

import java.util.Hashtable;

import au.edu.sydney.Curracurrong.datatype.DInteger32;
import au.edu.sydney.Curracurrong.datatype.DRecord;
import au.edu.sydney.Curracurrong.datatype.DString;
import au.edu.sydney.Curracurrong.datatype.Data;
import au.edu.sydney.Curracurrong.datatype.ParsingException;

/**
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public abstract class Sensor {
	int opID = -1;
	Hashtable propertyMap = null;
	
	void initialize(int opID, Hashtable propertyMap) {
		this.opID = opID;
		this.propertyMap = propertyMap;
	}
	
	public abstract DRecord read();
	
    Data getPropertyValue(String propertyKey) {
        Data value = null;
        // filter operator specific section
        if (propertyMap.containsKey(propertyKey)) {
            DRecord elem = (DRecord) propertyMap.get(propertyKey);
            try {
                DString readValue = (DString) elem.getElement(1);
                value = new DInteger32 (Integer.parseInt(readValue.tostring()));
            } catch (ParsingException ex) {
                System.out.println(ex);
            }
        }
        return value;
    }
}
