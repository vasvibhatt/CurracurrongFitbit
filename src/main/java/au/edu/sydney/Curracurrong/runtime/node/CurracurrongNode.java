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

package au.edu.sydney.Curracurrong.runtime.node;

import au.edu.sydney.Curracurrong.scheduler.Scheduler;
import au.edu.sydney.Curracurrong.util.NetworkAddress;
import au.edu.sydney.Curracurrong.runtime.Registry;
import au.edu.sydney.Curracurrong.runtime.SystemStatus;
import au.edu.sydney.Curracurrong.runtime.server.ServerAdministrator;
import au.edu.sydney.Curracurrong.runtime.server.ServerCommunicator;

/**
 * The startApp method of this class is called by the VM to start the
 * application.
 * 
 * The manifest specifies this class as MIDlet-1, which means it will
 * be selected for execution.
 * 
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public class CurracurrongNode {
	
    protected NodeAdministrator admin;
    protected NodeCommunicator comm;
    protected Scheduler sched;

    protected void startApp() {    
        System.out.println("Curracurrong Node starting up ...");

		String ourAddress = null;
		try {
			ourAddress = NetworkAddress.getAddress().getHostAddress();
		} catch (Exception e) {
			System.out.println("CASCADE: unable to fetch information about network interfaces");			
		}

        System.out.println("Our radio address = " + ourAddress);

        try {
            Thread.sleep(3000);
            System.out.println("start Node...");

            // create scheduler
            sched = Scheduler.getSchedulerInstance();
            System.out.println("new scheduler " + sched.toString());

            // create communicator
            comm = NodeCommunicator.getCommunicatorInstance();
            System.out.println("neww communicator " + comm.toString());

            // create administartor
            admin = NodeAdministrator.getAdministratorInstance();
            System.out.println("new administrator " + admin.toString());
            
            // Register the Administrator so that other components can find it if needed
    		Registry.getInstance().put("Administrator", admin);
    		Registry.getInstance().put("Communicator", comm);

            // run basestation units
            // run administrator unit
            admin.start();
            System.out.println("start administrator...");
            // run communicator
            comm.start();
            System.out.println("start communicator...");

            // initialize System status singleton
            SystemStatus.getSystemStatusInstance().getUpTime();

            // run scheduler
            // sched.start(); // this is now done in Administrator.run() after receiving a "commit" message
            System.out.println("postpone scheduler start...");
        } catch (Exception ex) {
            ex.printStackTrace();
        }             // cause the MIDlet to exit        
    }

    protected void pauseApp() {
        // This is not currently called by the Squawk VM
    }

    /**
     * Called if the MIDlet is terminated by the system.
     * I.e. if startApp throws any exception other than MIDletStateChangeException,
     * if the isolate running the MIDlet is killed with Isolate.exit(), or
     * if VM.stopVM() is called.
     * 
     * It is not called if MIDlet.notifyDestroyed() was called.
     *
     * @param unconditional If true when this method is called, the MIDlet must
     *    cleanup and release all resources. If false the MIDlet may throw
     *    MIDletStateChangeException  to indicate it does not want to be destroyed
     *    at this time.
     */
    protected void destroyApp(boolean unconditional) throws Exception {
        for (int i = 0; i < 8; i++) {
          //  leds[i].setOff();
        }
    }
    
    public static void main(String[] args) {
    	new CurracurrongNode().startApp();
    }
}
