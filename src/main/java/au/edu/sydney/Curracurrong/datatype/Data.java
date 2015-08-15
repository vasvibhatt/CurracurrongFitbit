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

import java.io.Serializable;

/**
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public abstract class Data implements Serializable {

    // return the data type of that data object
    abstract public String getType();

    // indicate wether primitive or composite data type
    abstract public boolean isPrimitive();

    // string presentation of the data
    abstract public String tostring();    


    /**
     * create a new data object from input string
     * @param type class name of datatype
     * @param value the value of the datatype
     * @return the data type object  or null  if there is not such a calss orvalue is not compatible with data type
     */
    
    public static Data createFromString(String type, String value) throws ParsingException
    {
        Data res;
        res = null;
        String s;
        boolean valid = false;


        if (type.equals("DInteger8"))
        {
            try {
                s = value;
                byte b_type = Byte.parseByte(s);
                res = new DInteger8(b_type);
                valid = true;
            }
            catch(NumberFormatException nf) {
                throw new ParsingException("Invalid string presentation");
            } 
            
        }

        if (type.equals("DInteger16"))
        {
            try {
                s = value;
                short sh_type = Short.parseShort(s);
                res = new DInteger16(sh_type);
                valid = true;
            }
            catch(NumberFormatException nf) {
                throw new ParsingException("Invalid string presentation");
            }
        }
        
        if (type.equals("DInteger32"))
        {
            try {
                s = value;
                int int_type = Integer.parseInt(s);
                res = new DInteger32(int_type);
                valid = true;
            }
            catch(NumberFormatException nf) {
                throw new ParsingException("Invalid string presentation");
            }
        }

         if (type.equals("DInteger64"))
        {
            try {
                s = value;
                long int_type = Long.parseLong(s);
                res = new DInteger64(int_type);
                valid = true;
            }
            catch(NumberFormatException nf) {
                throw new ParsingException("Invalid string presentation");
            }
        }


        if (type.equals("DFloat"))
        {
            try {
                s = value;
                float float_type = Float.parseFloat(s);
                res = new DFloat(float_type);
                valid = true;
                }
            catch (NumberFormatException nf) {
                throw new ParsingException("Invalid string presentation");
            }
        }

        if (type.equals("DString"))
        {
            s = value;
            res = new DString(s);
            valid = true;
        }

        // check for composite data types like DRecord and DArray
        if (valid == false)
        {
            int nType, nValue;
            boolean isRecord = false;
            boolean isArray = false;
            String listValue, listType,elemType, elemValue;;
            Data element;
            Parser p = new Parser();
            // DRecord
            try {
                listValue = value;
                listType = type;
                nType = p.getRecordLength(listType);
                nValue = p.getRecordLength(listValue);
                if (nType == nValue)
                {
                    isRecord = true;
                    DRecord recordObj = new DRecord(nType);
                    
                    for(int i = 0; i < nType; i++)
                    {
                        elemType = p.getFirstType(listType);
                        elemValue = p.getFirstType(listValue);
                        listType = p.getNext(listType);
                        listValue = p.getNext(listValue);
                        element = Data.createFromString(elemType, elemValue);
                        recordObj.setElement(i, element);
                    }
                    valid = true;
                    res = recordObj;
                }
                else
                    throw new ParsingException("");
                
            }
            catch (ParsingException pe) {
            }
            
            //DArray
            try {
                listValue = value;
                listType = type;
                nType = p.getArrayLength(listType);
                nValue = p.getArrayLength(listValue);
                if (nType == nValue)
                {
                    isArray = true;
                    DArray array = new DArray(nType);
                    for(int i = 0; i < nType; i++)
                    {
                        elemType = p.getFirstType(listType);
                        elemValue = p.getFirstType(listValue);
                        listType = p.getNext(listType);
                        listValue = p.getNext(listValue);
                        element = Data.createFromString(elemType, elemValue);
                        array.setElement(i, element);
                    }
                    valid = true;
                    res = array;
                }
                else
                    throw new ParsingException("");
            }
            catch (ParsingException pe) {

            }
        }

        if ( valid == false )
            throw new ParsingException("Incorrect format!");
        return res;
    }
}
