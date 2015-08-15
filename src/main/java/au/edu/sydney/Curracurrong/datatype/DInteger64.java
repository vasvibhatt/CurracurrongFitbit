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

package au.edu.sydney.Curracurrong.datatype;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

/**
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public class DInteger64 extends DInteger {
    private long value;

    /**
     * constructor 
     */
    public DInteger64() {
        value = (long) 0;
    }

    /**
     * constructor with passing value
     * @param sh value of DInteger16
     */
    public DInteger64(long n) {
        value = n;
    }

    /**
     * return the data type of DInteger16
     * @return data type DInteger16
     */
    public String getType() {
        return "DInteger64";
    }

    /**
     * set value of DInteger16
     * @param n new value
     */
    public void setValue(long n) {
        value = n;
    }

    /**
     * 
     */
    public long getValue() {
        return value;
    }

    /**
     * indicate whether primitive or composite data type
     * @return true DInteger16 is primitive
     */
    public boolean isPrimitive() {
        return true;
    }

    /**
     * get string presentation of DInteger16
     * @return string format of DInteger16
     */
    public String tostring() {
        return Long.toString(value);
    }


    /**
     * 
     * @param ois
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public void readObjectOnSensor(ObjectInputStream ois) throws IOException, ClassNotFoundException, IllegalAccessException,InstantiationException {
        this.value = ois.readLong();
    }

	/**
	 * @see nodes.spotSensor.java.io.KSNSerializableInterface#writeObjectOnSensor(ObjectOutputStream)
	 */
	public void writeObjectOnSensor(ObjectOutputStream oos) throws IOException {
            oos.writeLong(value);
    }
}
