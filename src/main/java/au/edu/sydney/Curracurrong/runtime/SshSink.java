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

import java.io.IOException;
import java.util.Hashtable;

import au.edu.sydney.Curracurrong.datatype.DRecord;
import au.edu.sydney.Curracurrong.datatype.Data;
import au.edu.sydney.Curracurrong.datatype.ParsingException;

/**
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public class SshSink extends Sink {
	void initialize(int opID, Hashtable propertyMap) {
        // call constructor of super-class to initialize
        // channels
        super.initialize(opID, propertyMap);	
	}
	
	@Override
	public void write(Data d) {
		System.out.println("Data to be written on remote machine");
		DRecord rec = (DRecord) d;
		Runtime run = Runtime.getRuntime();
		try{
		String reciever = rec.getElement(4).tostring();
		String sendTo = "ec2-user@" + reciever;
		long startTime = Long.parseLong(rec.getElement(3).tostring());
		//String command = "sudo echo " + startTime + " |  ssh -i curracurrongpem.pem " + sendTo +" -- 'cat >> /tmp/logfile; date >> /tmp/logfile > /dev/null >> &1'";
		// String command1 = "ssh -i curracurrongpem.pem " + sendTo + " -- echo " + startTime + " >> /tmp/logfile; java -jar /tmp/GetTimeMillis.jar >> /tmp/logfile 2>&1";
		String command1 = "ssh -i curracurrongpem.pem " + sendTo + " -- echo " + startTime + " $(($(date +%s%N)/1000000)) >> /tmp/logfile 2>&1";
		//String command2 = "ssh -i curracurrongpem.pem " + sendTo + " -- java -jar GetTimeMillis.jar >> /tmp/logfile 2>&1";
		System.out.println("Command to be executed --> " + command1);
		Process p = run.exec(command1);
		int exit = p.waitFor();
		System.out.println("Process1 returns --> " + exit);
		/*System.out.println("Command to be executed --> " + command2);
		p = run.exec(command2);
		exit = p.waitFor();
		System.out.println("Process2 returns --> " + exit);*/
		} catch(ParsingException pe) {
			pe.printStackTrace();
		} catch(IOException ie) {
			ie.printStackTrace();
		} catch(InterruptedException ie) {
			ie.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}	
	}
}
