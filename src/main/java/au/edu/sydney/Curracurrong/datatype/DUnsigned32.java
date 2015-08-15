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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
/**
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public class DUnsigned32 extends Data {

    private int value;

    public DUnsigned32() {
        
    }


    /**
     *
     * @return
     */
    public String getType() {
        return "";
    }


    /**
     * 
     * @return
     */
    public boolean isPrimitive() {
        return true;
    }

    /**
     *
     * @return
     */
    public String tostring() {
        return Integer.toString(value);
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
        this.value = ois.readInt();
    }

    /**
     * @see nodes.spotSensor.java.io.KSNSerializableInterface#writeObjectOnSensor(ObjectOutputStream)
     */
    public void writeObjectOnSensor(ObjectOutputStream oos) throws IOException {
        oos.writeInt(value);
    }
}
