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

package au.edu.sydney.Curracurrong.scheduler;

import java.util.LinkedList;
import au.edu.sydney.Curracurrong.runtime.StreamOperator;
import au.edu.sydney.Curracurrong.runtime.TimeTriggered;
import au.edu.sydney.Curracurrong.runtime.SystemStatus;


/**
 * This class managing and scheduling the execution of the stream operators based on their
 * scheduling semantic attribute. This thread sleeps whilst waiting for the time that the
 * next task is to be executed, saving precious CPU cycles. It also sleeps while
 * there are no tasks left to be executed at any point in time.
 * Scheduler is singleton class.
 *
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public class Scheduler extends Thread {

    /**
     * singleton instance of Scheduler
     */
    private static Scheduler instance = null;

    /**
     * A queue which stores all time-triggered operators
     */
    private RunQueue timeTriggeredTasks;

    /**
     * A queue which stores all idle event-triggered operators
     */
    private SchedulerQueue idle;

    /**
     * A queue which stores all ready event-triggered operators
     */
    private SchedulerQueue ready;


    /**
     * Returns a singleton instance of Scheduler
     * @return scheduler instance
     */
    public static Scheduler getSchedulerInstance(){
        if(instance == null){
            instance = new Scheduler();
        }
        return instance;
    }

    /**
     * Constructor initializes scheduler parameters
     */
    private Scheduler() {
        timeTriggeredTasks = new RunQueue();
        idle = new SchedulerQueue();
        ready = new SchedulerQueue();
    }

    /**
     * Returns the time difference in milliseconds between the given time and the
     * TimeKeeper's current time
     * @param time time to calculate distance from
     * @return (time - TIME)
     */
    private synchronized static long calculateTimeDelta(long time) {
	return (time - TimeKeeper.getInstance().getTimeMillis());
    }

    LinkedList<Thread> threads = new LinkedList<Thread>();

    class SchedulerThread implements Runnable {
    	StreamOperator operator = null;
    	public SchedulerThread(StreamOperator op) {
    		operator = op;
    	}
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			while (true) {
				operator.block();
				long startTime = System.nanoTime();
				// System.out.println("Before execute operator() ...");
				operator.execute();
				// System.out.println("After execute operator() ...");
				float interval = (float) ((System.nanoTime() - startTime) * 1000.0);
				SystemStatus.getSystemStatusInstance().addExecutionDuration(interval);
			}
		}
    }

    /**
     * This method is executed when the scheduling of tasks is to begin. It
     * creates an eternal loop, and then performs the appropriate tasks based on
     * what tasks are currently in the scheduler.
     */

    public void run() {
    	System.out.println(timeTriggeredTasks.size());
        System.out.println("Scheduler Started...." + timeTriggeredTasks.isEmpty());
        boolean kill = false;
        StreamOperator so;
        long delta = 0;

        // Asynchronously execute operators that are not time triggered operator
        // later we will do this in addIdleTask()
//        Enumeration<StreamOperator> operators = idle.elements();
//        while(operators.hasMoreElements()){
//            // Now launch a thread to run this operator in a loop
//            Thread thread = new Thread(new SchedulerThread(operators.nextElement()));
//            threads.add(thread);
//            thread.start();
//        }
        long sleepTime = 1;
        while(!kill){
            boolean flag = false;
            do {
                if (!timeTriggeredTasks.isEmpty() && flag == false) {
                    //System.out.println("timetriggered operator scheduled!");
                    StreamOperator streamOp = timeTriggeredTasks.getRootBlock();
                   
                    // REMOVE this comment after testing
                    delta = calculateTimeDelta(((TimeTriggered)streamOp).getStartTime());
                    //delta = -1;
                    if(delta < 0) {
                        int rep = ((TimeTriggered)streamOp).getRepetition();
                        if(rep > 0 && System.currentTimeMillis() < ((TimeTriggered)streamOp).getTimeOut()) {
                            long timeStamp = System.currentTimeMillis();
                            streamOp.execute();
                            SystemStatus.getSystemStatusInstance().addExecutionDuration(System.currentTimeMillis()-timeStamp);
                            long nextStartTime = ((TimeTriggered)streamOp).getStartTime() + ((TimeTriggered)streamOp).getTimeInterval();
                            ((TimeTriggered)streamOp).setStartTime(nextStartTime);
                            rep--;
                            ((TimeTriggered)streamOp).setRepeat(rep);
                        } else {
                            timeTriggeredTasks.remove((TimeTriggered)streamOp);
                        }
                    }
                } else {
                	// exponential sleep - think about this
                	sleepTime = ((sleepTime = ((sleepTime << 1) % 1000)) != 0) ? sleepTime : 1;
                	//sleepTime = delta;
                	try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
					}
                }
//                flag = true;
//                if(!ready.isEmpty() && flag == true){
//                    //System.out.println("EventTriggered operator scheduled!");
//                    StreamOperator streamOp = ready.peek();
//                    long timeStamp = System.currentTimeMillis();
//                    streamOp.execute();
//                    SystemStatus.getSystemStatusInstance().addExecutionDuration(System.currentTimeMillis()-timeStamp);
//                    ready.remove(streamOp);
//                }
//                flag = false;
            } while (!ready.isEmpty());
        }
        // Now interrupt all the running threads
        for (Thread thread : threads) {
			thread.interrupt();
			System.out.println("Running EventTriggered thread--");
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
    }
    
    /**
     * Adds new operator to idle queue
     * @param so stream operator
     */
    public synchronized void addIdleTask(StreamOperator so){
        idle.add(so);
		Thread thread = new Thread(new SchedulerThread(so));
		threads.add(thread);
		thread.start();
    }

    /**
     * Adds new operator to ready queue
     * @param so stream operator
     */
    public synchronized void addReadyTask(StreamOperator so){
        ready.add(so);
    }

    /**
     * Adds new time-triggered operator to queue
     * @param so stream operator
     */
    public synchronized void addTimeTriggeredTask(TimeTriggered so){
        timeTriggeredTasks.add(so);
    }

    /**
     * Checks if the stream operator is in ready queue
     * @param so stream operator
     * @return true if operator is in ready queue, false otherwise
     */
    public boolean isReady(StreamOperator so){
        return ready.contains(so);
    }

    /**
     * This method removes the operator from scheduler
     * @param so stream operator
     */
    public synchronized void removeOperator(StreamOperator so){
        if(idle.contains(so)) {
            idle.remove(so);
        } else if(isReady(so)) {
            ready.remove(so);
        } else if(timeTriggeredTasks.contains((TimeTriggered)so)) {
            timeTriggeredTasks.remove((TimeTriggered)so);
        } else {
            System.out.println("CASCADE: No operator entry found in Schduler queues");
        }
    }

    public synchronized void startSchedule() {
        if(!this.isAlive()) {
            SystemStatus.getSystemStatusInstance().addExecutionDuration(0);
            SystemStatus.getSystemStatusInstance().setUpTime(System.currentTimeMillis());
            this.start();
        }
    }
}