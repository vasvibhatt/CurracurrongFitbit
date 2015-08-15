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
 * This class presents DFloat which is floating point data type
 *
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public class DFloat extends Data {

    private float value;

    /**
     * constructor with default value
     */
    public DFloat() {
        value = 0;
    }

    public DFloat(Object o) {
        try {
            value = Float.parseFloat(o.toString());
        }
        catch(Exception e) {
            System.out.println("CASCADE: failed in DFloat, caught" + e);
        }

    }

    /**
     * constructor with value
     * @param f value of DFloat
     */
    public DFloat(float f) {
        value = f;
    }

    /**
     * return the data type of that data object
     * @return data type of the object DFloat
     */
    public String getType() {
        return "DFloat";
    }

    /**
     * indicate whether primitive or composite data type
     * @return true DFloat is primitive
     */
    public boolean isPrimitive() {
        return true;
    }

    /**
     * string presentation of Dfloat object
     * @return string of DFloat object
     */
    public String tostring() {
        return Float.toString(value);
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
        this.value = ois.readFloat();
    }

    /**
     * @see nodes.spotSensor.java.io.KSNSerializableInterface#writeObjectOnSensor(ObjectOutputStream)
     */
    public void writeObjectOnSensor(ObjectOutputStream oos) throws IOException {
        oos.writeFloat(value);
    }

}
