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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Hashtable;

import au.edu.sydney.Curracurrong.util.NetworkAddress;

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
public class ErrorFileSensor extends Sensor {
	
	private String errorFile = "/var/log/messages";
	private RandomAccessFile file = null;
	private long offset = 0;
	private DRecord record = null;
	private boolean timeTriggered = false;

	void initialize(int opID, Hashtable propertyMap) {
		super.initialize(opID, propertyMap);
        if (propertyMap.containsKey("file")) {
  	  		DRecord property = (DRecord) propertyMap.get("file");
  	  		try {
				errorFile = property.getElement(1).tostring();
			} catch (ParsingException e) {
				e.printStackTrace();
				System.out.println("Unable to parse property \"file\"");
			}
  	  	}
        if (propertyMap.containsKey("schedulingSemantic")) {
        	DRecord property = (DRecord) propertyMap.get("schedulingSemantic");
  	  		try {
				String scheduling = property.getElement(1).tostring();
				if (scheduling.equals("TimeTriggered")) {
					timeTriggered  = true;
				}
			} catch (ParsingException e) {
				e.printStackTrace();
				System.out.println("Unable to parse property \"file\"");
			}
        }
	}
	
	private int sleepInterval = 10;
	
	public int sleepTime() {
// TODO		sleep = ((sleep = (sleep << 1) % 1024) != 0)?sleep:1;
//		this.sleepInterval = (this.sleepInterval << 1) % 32;
//		this.sleepInterval = (this.sleepInterval != 0)?this.sleepInterval:1;
		return this.sleepInterval;
	}

	private boolean isTimeTriggered() {
		return this.timeTriggered;
	}
	
	private boolean isFileOpen() {
		if (file == null && errorFile != null) {
			try {
				file = new RandomAccessFile(errorFile, "r");
				offset = file.length();
				file.seek(offset);
			} catch (FileNotFoundException e) {
				file = null;
				offset = 0;
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				file = null;
				offset = 0;
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see runtime.Sensor#read()
	 */
	//@Override
	public DRecord read() {
		if (timeTriggered) {
			return read_time_triggered();
		} else {
			return read_event_triggered();
		}
	}
	
	public DRecord read_time_triggered() {
		return readRecord();
	}

	public DRecord read_event_triggered() {
		while (true) {
			if ((record = readRecord()) != null) {
				return record;
			} else {
				try {
					Thread.sleep(this.sleepTime());
				} catch (InterruptedException e) {
				}
			}
		}
	}

	public DRecord readRecord() {
		try {
			String line = null;
			if (isFileOpen() && (line = file.readLine()) != null) {
				offset = line.length() + 1; // including the new line

				record = new DRecord(7);

				record.setElement(0, new DFloat(SystemStatus.getSystemStatusInstance().getExecutionTime()));
				record.setElement(1, new DInteger64(System.currentTimeMillis()));
				record.setElement(2, new DString(NetworkAddress.getAddress().getHostAddress()));
				record.setElement(3, new DInteger64(SystemStatus.getSystemStatusInstance().getNumPacketsSent()));
				record.setElement(4, new DInteger64(SystemStatus.getSystemStatusInstance().getNumOfBytesSent()));
				record.setElement(5, new DInteger64(line.length()));
				record.setElement(6, new DString(line));

				return record;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
