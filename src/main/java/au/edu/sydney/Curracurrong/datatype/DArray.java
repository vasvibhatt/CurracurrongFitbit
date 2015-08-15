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
public class DArray extends Composite {

    private Data[] array;
    private int size;

    /**
     * constructor 
     * @param length number of elements in DArray
     */
    public DArray(int length) {
        array = new Data[length];
        size = length;
    }

    /**
     * return the data type of that data object
     * @return string presentation of type of DArray
     */
    public String getType() {
        return "DArray";
    }

    /**
     * indicate whether primitive or composite data type
     * @return false DArray is composite
     */
    public boolean isPrimitive() {
        return false;
    }

    /**
     * string presentation of DArray object
     * @return string equals to text presentation of DArray
     */
    public String tostring() {
        String str;
        String stp = "";
        for (int i = 0; i < size; i++)
        {
            if (!stp.equals(""))
                stp = stp + ",";
            str = array[i].tostring();
            stp = stp + str;
        }
        stp = "A(" + stp + ")";
        return stp;
    }


   
    /**
     * get the element at index equal idx
     * @param idx index of object
     * @return Data object
     * @throws ParsingException if given index is out of band
     */
    public Data getElement(int idx) throws ParsingException {
         // idx is in range
        if (idx <= size)
            return array[idx];
        else
            throw new ParsingException("out of band index in getElement()");
    }

    /**
     * set the passes element in specific index
     * @param idx index
     * @param element Data Object
     * @throws ParsingException if given index is out of band
     */
    public void setElement(int idx, Data element) throws ParsingException {
        if (idx <= size)
            array[idx] = element;
        else
            throw new ParsingException("out of band index in setElement()");
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
        this.size = ois.readInt();
        Object[] tmp = new Object[this.size];
        tmp = (Object[]) ois.readObject();
        for (int i = 0; i<this.size; i++) {           
            this.array[i] = (Data) tmp[i];
        }
    }

    /**
     *
     * @param oos
     * @throws IOException
     */
    public void writeObjectOnSensor(ObjectOutputStream oos) throws IOException {
        oos.writeInt(size);
        oos.writeObject(array);
    }
}
