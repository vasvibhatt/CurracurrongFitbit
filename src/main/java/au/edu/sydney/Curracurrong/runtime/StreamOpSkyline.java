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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;

import au.edu.sydney.Curracurrong.datatype.DFloat;
import au.edu.sydney.Curracurrong.datatype.DInteger64;
import au.edu.sydney.Curracurrong.datatype.DRecord;
import au.edu.sydney.Curracurrong.datatype.DString;
import au.edu.sydney.Curracurrong.datatype.Data;
import au.edu.sydney.Curracurrong.datatype.ParsingException;

/**
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public class StreamOpSkyline extends StreamOpFilter {
	/* Default window size is set to 1000 */
	private static final int DEFAULT_WINDOW_SIZE = 1000;
	
	/**
	 * Index of the fields in the record @see DefaultSensor
	 */
	private static final int CPU_UTIL_INDEX = 5;

	private static final int MINIMUM = 0;

	private static final int MAXIMUM = 1;
	
    /**
     * number of data elements for average
     */
    private int windowSize = DEFAULT_WINDOW_SIZE;
    
    private long recordNum = 0;
    
    private long lastTime = 0;

    Map<String, ArrayList<Double>> bounds = new HashMap<String, ArrayList<Double>>(); 

    enum WindowType {
    	MESSAGE_WINDOW,
    	TIME_WINDOW
    };
    WindowType windowType = WindowType.MESSAGE_WINDOW;
    /**
     * Initializes the stream operator
     * @param opID operator id
     * @param propertyMap property list
     */
    public void initialize(int opID, Hashtable propertyMap) {
        // call constructor of super-class to initialize
        // channels
        super.initialize(opID, propertyMap);
        
		windowType = WindowType.MESSAGE_WINDOW;
		DRecord property = (DRecord) propertyMap.get("type");
		try {
			String type = property.getElement(1).tostring();
			if (type != null && type.compareTo("time") == 0) {
				windowType = WindowType.TIME_WINDOW;        	
			} else if (type != null && type.compareTo("message") == 0) {
				windowType = WindowType.MESSAGE_WINDOW;
			}
		} catch (Exception e) { /* ... do nothing */}
		
        Data value = getPropertyValue("window");
        if (value != null) {
        	// the window size is in number of messages or time in milliseconds
            windowSize = Integer.parseInt(value.tostring());
            lastTime = System.currentTimeMillis();
            lastTime = lastTime - (lastTime % windowSize) + windowSize;
        }
        
        bounds.put("cpu", new ArrayList<Double>());
        bounds.get("cpu").add(MINIMUM, Double.MAX_VALUE);
        bounds.get("cpu").add(MAXIMUM, Double.MIN_VALUE);
    }

    private void reset() {
		bounds.get("cpu").set(MINIMUM, Double.MAX_VALUE);
		bounds.get("cpu").set(MAXIMUM, Double.MIN_VALUE);
    }
    
	private void emit() {
		try {
			DRecord outRec = new DRecord(2);
			outRec.setElement(0, new DFloat(bounds.get("cpu").get(MINIMUM)));
			outRec.setElement(1, new DFloat(bounds.get("cpu").get(MAXIMUM)));
			if (outRec.getSize() > 0) {
				send(outRec);
			}
		} catch (ParsingException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see runtime.StreamOperator#execute()
	 */
	@Override
	public void execute() {
		if(isDataAvailable(0)) {
			// DRecord rec = (DRecord) peek(0);
			DRecord rec = (DRecord) receive(0);
			
			// TODO: For now we take the CPU utilization [] and record the envelope values for it
			//       in the future we will send envelope value for all fields.		
			String cpuData = null;
			double cpuUtil = 0.0;
			try {
				cpuData = rec.getElement(CPU_UTIL_INDEX).tostring();
				cpuUtil = Double.parseDouble(cpuData);
			} catch (ParsingException e) {
				e.printStackTrace();
			}

			if (bounds.get("cpu").get(MINIMUM).doubleValue() > cpuUtil) {
				bounds.get("cpu").set(MINIMUM, Double.valueOf(cpuUtil));
			}
			if (bounds.get("cpu").get(MAXIMUM).doubleValue() < cpuUtil) {
				bounds.get("cpu").set(MAXIMUM, Double.valueOf(cpuUtil));
			}
				
			if (windowType == WindowType.MESSAGE_WINDOW) {
				if (++recordNum == windowSize) {
					// reset the record window
					recordNum = 0;
					emit();
					reset();
				}
			} else {
				long currentTime = System.currentTimeMillis();
				if (currentTime >= lastTime + windowSize) {
					// reset the record window
					lastTime = currentTime - (currentTime % windowSize) + windowSize;
					emit();
					reset();
				}
			}
		}	
	}
}