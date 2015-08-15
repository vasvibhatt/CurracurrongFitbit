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
import java.util.Vector;

public class Parser {
     public Parser()
        {
        }

     /**
      * get the string presentation of record and return the number of record elements
      * example: input R[i1(2),f(3.5)] return 2
      * @param input -- string presentation of DRecord
      * @return number of record elements
      * @throws ParsingException
      */
     public int getRecordLength(String input) throws ParsingException
        {
            int numElements;
            int size = input.length();
            int open = 0;
            int close = 0;
            int items = 0;

            if (input.charAt(0) == 'R' && input.charAt(1) == '[' && input.charAt(size-1) == ']' )
            {
                for(int i = 1; i < size; i++)
                {
                    if (input.charAt(i) == '[')
                        open++;
                    if (input.charAt(i) == ']')
                        close++;
                    if (input.charAt(i) == ',')
                        if ((open - close) == 1)
                           items++;
                }
                if ((open == close) && size > 3)
                    items++;
                else
                    if ((open == close) && size == 3)
                        items = 0;
                    else
                        throw new ParsingException("incorrect DRecord format");
            }
            else
            {
                throw new ParsingException("incorrect DRecord format");
            }

            return items;
        }

    /**
     * get the string presentation of array and return the number of record elements
     * example: input A[i1(2),f(3.5)] return 2
     * @param input -- string presentation of DArray
     * @return number of array elements
     * @throws ParsingException
     */
     public int getArrayLength(String input) throws ParsingException
        {
            int numElements;
            int size = input.length();
            int open = 0;
            int close = 0;
            int items = 0;

            if (input.charAt(0) == 'A' && input.charAt(1) == '[' && input.charAt(size-1) == ']' )
            {
                for(int i = 1; i < size; i++)
                {
                    if (input.charAt(i) == '[')
                        open++;
                    if (input.charAt(i) == ']')
                        close++;
                    if (input.charAt(i) == ',')
                        if ((open - close) == 1)
                           items++;
                }
                if ((open == close) && (size > 3))
                    items++;
                else
                    if ((size == 3) && (open == 1) && (open == close))
                        items = 0;
                    else
                        throw new ParsingException("incorrect DRecord format");
            }
            else
            {
                throw new ParsingException("incorrect DRecord format");
            }

            return items;
        }

     /**
      * get the string presentation of record or array and return the string presentation of its first element
      * example: input R[i2(5), R[i1(2),i2(34)]] return i2(5)
      * @param list -- string presentation of record or array
      * @return string presentation of its first element
      * @throws ParsingException
      */
     public String getFirstType(String list) throws ParsingException
     {
         int pos;
         String type;
         if (list.charAt(1) == '[')
         {
             int open, close;
             open = 1;
             close = 0;
             pos = 1;
             while (open > close)
             {
                 pos++;
                 if (list.charAt(pos) == '[')
                     open++;
                 if (list.charAt(pos) == ']')
                     close++;
                 if ((list.charAt(pos) == ',') && (open - close == 1))
                     break;

             }
             type = list.substring(2, pos);
         }
         else
         {
            pos = 1;
            while (list.charAt(pos) != ',' || list.charAt(pos) != ']')
            {
                pos++;
            }
            type = list.substring(1, pos);
         }
         return type;
     }

     /**
      * remove the first element from the string presentation Record or Array list and return the 
      * rest in string format
      * @param string presentation of Record or Array
      * @return string presentation of record or array after removing its first element
      */
     public String getNext(String list)
     {
         String first = "";
         try {
             first = getFirstType(list);
         }
         catch (ParsingException pe) {

         }
         int len = first.length();
         len++;
         // R[ at start
         len = len + 2;
         String str = list;
         str = list.substring(len);
         if (list.charAt(0) == 'R')
            str = "R[" + str;
         else
            str = "A[" + str;

         return str;
     }     
}
