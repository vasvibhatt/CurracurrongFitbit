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

import au.edu.sydney.Curracurrong.datatype.DRecord;
import au.edu.sydney.Curracurrong.datatype.DString;
import au.edu.sydney.Curracurrong.datatype.Data;
import au.edu.sydney.Curracurrong.datatype.ParsingException;

import java.io.*;
import java.util.Hashtable;

/**
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public class StreamOpSink extends StreamOperator {
	private PrintStream out;

	// count receive packets
	private int counter;

	private static final String DEFAULT_SINK_OPERATOR = "au.edu.sydney.Curracurrong.runtime.DefaultSink";
	private Sink sink;

	/**
	 * This method initializes Sink operator parameters
	 * 
	 * @param opID Operator Id
	 */
	public void initialize(int opID, Hashtable propertyMap) {
		super.initialize(opID, propertyMap);
		// initialize CSV file
		super.initialize(opID, propertyMap);

		try {
			if (propertyMap.containsKey("sink")) {
				DRecord property = (DRecord) propertyMap.get("sink");
				DString sensorName;
				try {
					sensorName = (DString) property.getElement(1);
					sink = (Sink) Class.forName(
							"runtime." + sensorName.tostring()).newInstance();
				} catch (ParsingException e) {
					e.printStackTrace();
					sink = (Sink) Class.forName(DEFAULT_SINK_OPERATOR)
							.newInstance();
				}
			} else {
				sink = (Sink) Class.forName(DEFAULT_SINK_OPERATOR)
						.newInstance();
			}
			sink.initialize(opID, propertyMap);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		counter = 0;
	}

	/**
	 * This method executes the Sink operator
	 */
	public synchronized void execute() {
		String a;
		int size = getInputChannel(0).size();

		for (int j = 0; j < size; j++) {
			Data d = ((Data) peek(0, j));
			if (d != null) {
				a = d.tostring();
				System.out.println("Data to be written in file:" + a);
				sink.write(d);
				// out.println(a);
				receive(0);
				counter++;
			}
		}
	}
}
