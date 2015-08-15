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

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Random;
import java.util.StringTokenizer;

import au.edu.sydney.Curracurrong.datatype.DRecord;
import au.edu.sydney.Curracurrong.datatype.Data;
import au.edu.sydney.Curracurrong.datatype.ParsingException;

/**
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public class DefaultSink extends Sink {
    private PrintStream out;

    // Default directory for the results NOTE: should be the same as querylanguage.Constants.RESULTS_DIR
	public static final String RESULTS_DIR = "/tmp/";
	
	private String logFile = RESULTS_DIR + "Output_" + new Random().nextInt(5000) + ".csv";

	void initialize(int opID, Hashtable propertyMap) {
		super.initialize(opID, propertyMap);
        if (propertyMap.containsKey("logfile")) {
  	  		DRecord property = (DRecord) propertyMap.get("logfile");
  	  		try {
				logFile = property.getElement(1).tostring();
			} catch (ParsingException e) {
				e.printStackTrace();
				System.out.println("Unable to parse property \"file\"");
			}
  	  	}
        createFile();
	}

	/* (non-Javadoc)
	 * @see runtime.Sink#read()
	 */
	@Override
	public void write(Data d) {
		synchronized (this) {
			DRecord rec = (DRecord) d;
			out.println("[" + System.currentTimeMillis() + "] " + rec.tostring());
		}
	}
	
	/**
	 * This method creates CSV file
	 */
	private void createFile() {
		try {
			FileOutputStream fstream = new FileOutputStream(logFile);
			out = new PrintStream(fstream);
		} catch (Exception e) {
			System.out.println("Cascade: Failed in createFile()");
			e.printStackTrace();
		}
	}
}
