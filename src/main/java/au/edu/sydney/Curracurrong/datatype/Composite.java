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

/**
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public abstract class Composite extends Data {

    /**
     * get the element at index equal idx
     * @param idx index of object
     * @return Data object 
     * @throws ParsingException if given index is out of band
     */
    abstract public Data getElement(int idx) throws ParsingException;

    /**
     * set the passes element in specific index
     * @param idx index
     * @param element Data Object
     * @throws ParsingException if given index is out of band
     */
    abstract public void setElement(int idx, Data element) throws ParsingException;   
}
