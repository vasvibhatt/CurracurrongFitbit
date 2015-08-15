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

import au.edu.sydney.Curracurrong.runtime.server.ServerAdministrator;
import au.edu.sydney.Curracurrong.runtime.server.ServerCommunicator;

/**
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public class RadioTimer extends Thread{
    private static RadioTimer instance;
    private long sleepTime;
    private long commTime;
    private boolean isOn;

    public static RadioTimer getInstance() {
        return instance;
    }

    public static RadioTimer getInstance(long sleep, long comm) {
        if(instance == null) {
            instance = new RadioTimer(sleep, comm);
        }
        return instance;
    }
    private RadioTimer(long sleep, long comm) {
        long delay = TimeTriggered.getCommDelay();
        sleepTime = sleep - delay;
        commTime = comm + delay;
    }

    public void run() {
        long delay = TimeTriggered.getCommDelay();
        sleepTime = sleepTime - delay;
        commTime = commTime + delay;
        System.out.println("Sleep time " + sleepTime + " comm time " + commTime + " with delay " + delay);
        while(true) {
            try{
                synchronized(this) {
                    System.out.println("comm time in TimeKeeper " + this.getName());
                    try{
                        ServerCommunicator.getCommunicatorInstance().setConnectionClosed(false);
                        ServerAdministrator.getAdministratorInstance().setConnectionClosed(false);
                        isOn = true;
                        this.sleep(commTime*1000);
                        //this.sleep(10000);
                        }
                    catch(InterruptedException e) {
                        System.err.println("error in opening connection from timer");
                    }
                }
                synchronized(this){
                    System.out.println("sleep time in TimeKeeper " + this.getName());
                    try{
                        ServerCommunicator.getCommunicatorInstance().setConnectionClosed(true);
                        ServerAdministrator.getAdministratorInstance().setConnectionClosed(true);
                        isOn = false;
                        this.sleep(sleepTime*1000);
                        //this.sleep(30000);
                    }
                    catch(InterruptedException e) {
                        System.err.println("error in closing connection from timer");
                    }
                }
            }
            catch(Exception ie) {
                System.out.println("CASCADE: Timer thread interrupted");
            }
        }
    }

    public boolean isRadioOn() {
        return isOn;
    }
}
