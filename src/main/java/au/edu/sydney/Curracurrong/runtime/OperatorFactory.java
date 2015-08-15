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


/**
 * This class is responsible for creating stream operators and scheduling semantic objects
 * it is separated from administrative class because of separation of concerns to separate the responsibility
 * of complex creation and hide creation logic from administrative class (factory pattern)
 *
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public class OperatorFactory {

    public static StreamOperator streamOperator;


    /**
     * This method returns the stream operator from the operator factory
     * @param operatorType type of the operator
     * @return Stream operator reference
     */
    public static StreamOperator getStreamOperator(String operatorType)
    {
        String type = operatorType.substring("StreamNode".length());
        operatorType = "StreamOp" + type;
        try {
            String className = "au.edu.sydney.Curracurrong.runtime." + operatorType;
            streamOperator = (StreamOperator) Class.forName(className).newInstance();
        }
        catch (InstantiationException e) {
            System.out.println("CASCADE: failed in Operatorfactory caught" + e);
        }
        catch (IllegalAccessException f) {
            System.out.println("CASCADE: failed in Operatorfactory caught" + f);
        }
        catch (ClassNotFoundException c) {
            System.out.println("CASCADE: failed in Operatorfactory caught" + c);
        }
        catch (Exception ei) {
            System.out.println("CASCADE: failed in Operatorfactory caught" + ei);
        }
        return streamOperator;
    }
}
