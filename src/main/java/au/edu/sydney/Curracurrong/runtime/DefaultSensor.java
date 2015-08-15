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

import java.util.Hashtable;

import au.edu.sydney.Curracurrong.util.NetworkAddress;

import au.edu.sydney.Curracurrong.datatype.DFloat;
import au.edu.sydney.Curracurrong.datatype.DInteger64;
import au.edu.sydney.Curracurrong.datatype.DRecord;
import au.edu.sydney.Curracurrong.datatype.DString;

/**
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public class DefaultSensor extends Sensor {
    // should change after first test to DReord
    // length 14 = List Of Columns - > Sender,Receiver,ExecTime,UpTime,NumPackSent,NumByteSent,CPUUtil,DiskRead,DiskWrite,DiskReadByte,DiskWriteByte,NetworkIn,NetworkOut,statusCheckFailed,statusChkFailedInst,statusChkFailedSys
    private DRecord sense;

	/* (non-Javadoc)
	 * @see runtime.Sensor#initialize(int, java.util.Hashtable)
	 */
	@Override
	void initialize(int opID, Hashtable propertyMap) {
		super.initialize(opID, propertyMap);
		
	}
	
	/* (non-Javadoc)
	 * @see runtime.Sensor#read()
	 */
	@Override
	public DRecord read() {
        senseAll();
        return sense;
 	}

    /**
     * Collects reading from all sensors: Temperature, Light, x, y & z
     */
    private void senseAll() {
        try {
        	// TODO: this is the sense operator that needs to be implemented
        	//       this will be replaced by sense for host information from /proc
            sense = new DRecord(15);
            sense.setElement(0, new DFloat(SystemStatus.getSystemStatusInstance().getExecutionTime()));

            //sense.setElement(1, new DInteger64(SystemStatus.getSystemStatusInstance().getUpTime()));
            sense.setElement(1, new DInteger64(System.currentTimeMillis()));
            
            sense.setElement(2, new DString(NetworkAddress.getAddress().getHostAddress()));
            sense.setElement(3, new DInteger64(SystemStatus.getSystemStatusInstance().getNumPacketsSent()));
            sense.setElement(4, new DInteger64(SystemStatus.getSystemStatusInstance().getNumOfBytesSent()));


            sense.setElement(5, new DFloat(SystemStatus.getSystemStatusInstance().getCPUUtilization()));
            sense.setElement(6, new DInteger64(SystemStatus.getSystemStatusInstance().getDiskReadOps()));
            sense.setElement(7, new DInteger64(SystemStatus.getSystemStatusInstance().getDiskWriteOps()));
            sense.setElement(8, new DInteger64(SystemStatus.getSystemStatusInstance().getDiskReadBytes()));
            sense.setElement(9, new DInteger64(SystemStatus.getSystemStatusInstance().getDiskWriteBytes()));
            sense.setElement(10, new DInteger64(SystemStatus.getSystemStatusInstance().getNetworkIn()));
            sense.setElement(11, new DInteger64(SystemStatus.getSystemStatusInstance().getNetworkOut()));
            sense.setElement(12, new DInteger64(SystemStatus.getSystemStatusInstance().getStatusCheckFailed()));
            sense.setElement(13, new DInteger64(SystemStatus.getSystemStatusInstance().getStatusCheckFailed_Instance()));
            sense.setElement(14, new DInteger64(SystemStatus.getSystemStatusInstance().getStatusCheckFailed_System()));
            System.out.println("Sensed data -->" + sense.tostring());
        } catch(Exception e) {
        	e.printStackTrace();
            System.out.println("CASCADE: sense operation failed");
        }
    }
}