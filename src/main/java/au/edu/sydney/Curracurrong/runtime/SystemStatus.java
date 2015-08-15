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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public class SystemStatus {

	/**
     * Singleton instance of SystemStatus
     */
    private static SystemStatus instance;

    /**
     * time to execute stream operators so far
     */
    private float executionTime;

    /**
     * System up-time
     */
    private long upTime;

    /**
     * number of packets sent so far
     */
    private long numOfPacketsSent;

    /**
     * number of bytes sent so far
     */
    private long numOfBytesSent;

    /**
     * Return singleton instance of SystemStatus
     * @return instance
     */
    public static SystemStatus getSystemStatusInstance() {
        if(instance == null) {
            instance = new SystemStatus();
        }
        return instance;
    }

    /**
     * Constructor
     */
    private SystemStatus() {
        this.executionTime = 0;
        this.upTime = System.currentTimeMillis();
        this.numOfBytesSent = 0;
        this.numOfPacketsSent = 0;
        // getCPUUtilization(); // call to initialize the previous settings
    }

    /**
     * Return execution time
     * @return exectionTime
     */
    public float getExecutionTime() {
        return executionTime;
    }

    /**
     * Return System Up-time
     * @return upTime
     */
    public long getUpTime() {
        return System.currentTimeMillis() - upTime;
    }

    /**
     * Set System up-time
     * @param time
     */
    public void setUpTime(long time) {
        this.upTime = time;
    }

    /**
     * Add operator execution time to System
     * @param delta execution time
     */
    public void addExecutionDuration(float delta) {
        this.executionTime = this.executionTime + delta;
    }

    /**
     * Return number of packets sent so far
     * @return number of packets
     */
    public long getNumPacketsSent() {
        return numOfPacketsSent;
    }

    /**
     * Return number of bytes sent so far
     * @return number of bytes
     */
    public long getNumOfBytesSent() {
        return numOfBytesSent;
    }

    /**
     * Set number of packets sent
     * @param numPackets
     */
    public void setNumPacketSent(long numPackets) {
        this.numOfPacketsSent = numPackets;
    }

    /**
     * Set number of bytes sent
     * @param numPacket
     */
    public void setnumOfBytesSent(long numPacket) {
        this.numOfBytesSent = numPacket;
    }

    /*
     * The following private members are for CPU utilization
     */
    private static final String STAT_FILE = "/proc/stat";
	private long cpu_user = 0;
	private long cpu_nice = 0;
	private long cpu_sys = 0;
	private long cpu_idle = 0;
	private long cpu_iowait = 0;
	private long cpu_hardirq = 0;
	private long cpu_softirq = 0;
	private long cpu_steal = 0;
	@SuppressWarnings("unused")
	private long cpu_guest = 0;
	@SuppressWarnings("unused")
	private long cpu_guest_nice = 0;
	private long uptime = 0;
	private long prev_cpu_idle = 0;
	private long prev_uptime = 0;
	/**
	 * CPUUtilization:
	 * The percentage of allocated EC2 compute units that are currently in use on the instance. This metric
	 * identifies the processing power required to run an application upon a selected instance.
	 * 
	 * Units: Percent
	 * 
	 * @return percentage of allocated EC2 compute units that are currently in use
 	 */
	public double getCPUUtilization() {
		System.out.println("In SystemStatus.getCPUUtilization()");
		String line = null;
		FileInputStream input = null;
		try {
			input = new FileInputStream(new File(STAT_FILE));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(input);
		while ((line = scanner.nextLine()) != null) {
		    // Deal with the line
			prev_uptime = uptime;
			prev_cpu_idle = cpu_idle;
			if (line.substring(0, 4).compareTo("cpu ") == 0) {
				/*
				 * Read the number of jiffies spent in the different modes
				 * (user, nice, etc.) among all proc. CPU usage is not reduced
				 * to one processor to avoid rounding problems.
				 */
				String remaining = line.substring(5, line.length());
				String [] tokens = remaining.split("[\t ]+");
				cpu_user = Long.parseLong(tokens[0]);
				cpu_nice = Long.parseLong(tokens[1]);
				cpu_sys = Long.parseLong(tokens[2]);
				cpu_idle = Long.parseLong(tokens[3]);
				cpu_iowait = Long.parseLong(tokens[4]);
				cpu_hardirq = Long.parseLong(tokens[5]);
				cpu_softirq = Long.parseLong(tokens[6]);
				cpu_steal = Long.parseLong(tokens[7]);
				cpu_guest = Long.parseLong(tokens[8]);
				cpu_guest_nice = Long.parseLong(tokens[9]);

				/*
				 * Compute the uptime of the system in jiffies (1/100ths of a second
				 * if HZ=100).
				 * Machine uptime is multiplied by the number of processors here.
				 *
				 * NB: Don't add cpu_guest/cpu_guest_nice because cpu_user/cpu_nice
				 * already include them.
				 */
				uptime = cpu_user + cpu_nice + cpu_sys + cpu_idle + cpu_iowait + cpu_hardirq + cpu_steal + cpu_softirq;
				if (uptime - prev_uptime > 0) {
					return (100.0 - (100.0 * (double)(cpu_idle - prev_cpu_idle))/((double)(uptime - prev_uptime)));
				}
			}
		}

		// Done with the file
		try{
			input.close();
			input = null;
			scanner.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("Done SystemStatus.getCPUUtilization()");
		
		return 0;
	}

	private static final boolean IGNORE_VIRTUAL_DEVICES = true;
	private static final String DISKSTATS = "/proc/diskstats";
	private static final String SYSFS_BLOCK = "/sys/block";
	private static final long BYTES_PER_SECTOR = 512;

	private static boolean is_device(String name, boolean allow_virtual)
	{
		/* Some devices may have a slash in their name (eg. cciss/c0d0...) */
		return new File(SYSFS_BLOCK + "/" + name.replace('/', '!') + (allow_virtual ? "" : "/device")).exists();
	}

	private long rd_ios = 0;
	private long wr_ios = 0;
	private long rd_sec = 0;
	private long wr_sec = 0;
	private long dk_drive_rio = 0;
	private long dk_drive_rblk = 0;
	private long dk_drive_wio = 0;
	private long dk_drive_wblk = 0;

	/**
	 * DiskReadOps:
	 * Completed read operations from all ephemeral disks available to the instance (if your instance uses
	 * Amazon EBS, see Amazon EBS Metrics.)
	 * 
	 * This metric identifies the rate at which an application reads a disk. This can be used to determine
	 * the speed in which an application reads data from a hard disk.
	 * 
	 * Units: Count
	 * 
	 * At this point, we perform the task of fetching all the disk I/O statistics when getDiskReadOps() is called.
	 * The remaining disk I/O stats functions use the statistics fetched by this function.
	 * 
	 * @return completed read operations from all ephemeral disks since system start up
	 */
	public long getDiskReadOps() {
		String line = null;
		FileInputStream input = null;
		try {
			input = new FileInputStream(new File(DISKSTATS));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Scanner scanner = new Scanner(input);
		try {
			dk_drive_rio = dk_drive_rblk = dk_drive_wio = dk_drive_wblk = 0;
			while ((line = scanner.nextLine()) != null) {
				String dev_name = null;				
				String[] tokens = line.split("[\\s]+");
				
				if (tokens.length == 15) {
					dev_name = tokens[3];
					rd_ios = Long.parseLong(tokens[4]);
					rd_sec = Long.parseLong(tokens[6]);
					wr_ios = Long.parseLong(tokens[8]);
					wr_sec = Long.parseLong(tokens[10]);

					if (is_device(dev_name, IGNORE_VIRTUAL_DEVICES)) {
						/*
						 * OK: It's a (real) device and not a partition. Note:
						 * Structure should have been initialized first!
						 */
						dk_drive_rio += rd_ios;
						dk_drive_rblk += rd_sec * BYTES_PER_SECTOR; // verify ... not sure this needs to be multiplied
						dk_drive_wio += wr_ios;
						dk_drive_wblk += wr_sec * BYTES_PER_SECTOR; // verify ... not sure this needs to be multiplied
					}
				}
			}
		} catch (NoSuchElementException e) {
			// TODO ignore for now ... to deal with later
		}
		
//		printf("dreads = %ld dwrites = %ld dreadbytes = %ld dwritebytes = %ld\n",
//					dk_drive_rio, dk_drive_wio, dk_drive_rblk, dk_drive_wblk);

		return dk_drive_rio;
	}
	
	/**
	 * DiskWriteOps:
	 * Completed write operations to all ephemeral disks available to the instance (if your instance uses
	 * Amazon EBS, see Amazon EBS Metrics.)
	 * This metric identifies the rate at which an application writes to a hard disk. This can be used to
	 * determine the speed in which an application saves data to a hard disk.
	 * 
	 * Units: Count
	 * 
	 * Assumes that getDiskReadOps() was called to refresh the counts.
	 * 
	 * @return Completed write operations to all ephemeral disks available since system start up
	 */
	public long getDiskWriteOps() {
		return dk_drive_wio;
	}

	/**
	 * DiskReadBytes:
	 * Bytes read from all ephemeral disks available to the instance (if your instance uses Amazon EBS,
	 * see Amazon EBS Metrics.)
	 * This metric is used to determine the volume of the data the application reads from the hard disk
	 * of the instance. This can be used to determine the speed of the application.
	 * 
	 * Units: Bytes
	 * 
	 * Assumes that getDiskReadOps() was called to refresh the counts.
	 * 
	 * @return Bytes read from all ephemeral disks available to the instance since system start up
	 */
	public long getDiskReadBytes() {
		return dk_drive_rblk;
	}

	/**
	 * DiskWriteBytes:
	 * Bytes written to all ephemeral disks available to the instance (if your instance uses Amazon EBS,
	 * see Amazon EBS Metrics.) This metric is used to determine the volume of the data the application
	 * writes onto the hard disk of the instance. This can be used to determine the speed of the application.
	 * 
	 * Units: Bytes 
	 * 
	 * Assumes that getDiskReadOps() was called to refresh the counts.
	 * 
	 * @return Bytes written to all ephemeral disks since system start up
	 */
	public long getDiskWriteBytes() {
		return dk_drive_wblk;
	}

	/*
     * This document is a good starting point to understand what the fields of the /proc/net/dev file
     * signifies:
     *     http://www.onlamp.com/pub/a/linux/2000/11/16/LinuxAdmin.html
	 */
	private long rx_bytes = 0;
	private long rx_packets = 0;
	private long rx_compressed = 0;
	private long multicast = 0;
	private long tx_bytes = 0;
	private long tx_packets = 0;
	private long tx_compressed = 0;
	private static final String NET_DEV = "/proc/net/dev";

	/**
	 * NetworkIn:
	 * The number of bytes received on all network interfaces by the instance. This metric identifies the
	 * volume of incoming network traffic to an application on a single instance.
	 * 
	 * Units: Bytes
	 * @return total bytes received by all network devices
	 */
	public long getNetworkIn() {
		String line = null;
		FileInputStream input = null;
		try {
			input = new FileInputStream(new File(NET_DEV));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Scanner scanner = new Scanner(input);
		try {
			// reset all counts
			rx_bytes = rx_packets = rx_compressed = multicast = tx_bytes = tx_packets = tx_compressed = 0;
			
			while ((line = scanner.nextLine()) != null) {
				if (!line.contains(":"))	// the header line does not contain a :
					continue;				// skip the header line
				// System.out.println("[" + line + "]");
				// System.out.println("[" + line + "]");
				String[] tokens = line.split("[\\s]+");
				// System.out.println("num tokens = [" + tokens.length + "] token 0 is [" + tokens[0] + "]");
				rx_bytes += Long.parseLong(tokens[2]);
				rx_packets += Long.parseLong(tokens[3]);
				rx_compressed += Long.parseLong(tokens[8]);
				multicast += Long.parseLong(tokens[9]);
				tx_bytes += Long.parseLong(tokens[10]);
				tx_packets += Long.parseLong(tokens[11]);
				tx_compressed += Long.parseLong(tokens[17]);
			}
		} catch (NoSuchElementException e) {
			// TODO ignore for now ... to deal with later
		}

		return rx_bytes;
	}

	/**
	 * NetworkOut:
	 * The number of bytes sent out on all network interfaces by the instance. This metric identifies the
	 * volume of outgoing network traffic to an application on a single instance.
	 * 
	 * Units: Bytes
	 * @return total bytes sent by all network devices
	 */
	public long getNetworkOut() {
		return tx_bytes;
	}

	/**
	 * NetworkPacketsIn:
	 * The number of packets received on all network interfaces by the instance. This metric identifies the
	 * volume of incoming network traffic to an application on a single instance.
	 * 
	 * Units: Bytes
	 * @return total bytes received by all network devices
	 */
	public long getNetworkPacketsIn() {
		return rx_packets;
	}

	/**
	 * NetworkPacketsOut:
	 * The number of packets sent out on all network interfaces by the instance. This metric identifies the
	 * volume of outgoing network traffic to an application on a single instance.
	 * 
	 * Units: Bytes
	 * @return total bytes sent by all network devices
	 */
	public long getNetworkPacketsOut() {
		return tx_packets;
	}

	/**
	 * NetworkCompressedIn:
	 * The number compressed packets received on all network devices by the instance. This metric
	 * identifies the volume of incoming network traffic to an application on a single instance.
	 * 
	 * Units: Bytes
	 * @return total compressed packets received by all network devices
	 */
	public long getNetworkCompressedIn() {
		return rx_compressed;
	}

	/**
	 * NetworkCompressedOut:
	 * The number compressed packets sent out on all network devices by the instance. This metric
	 * identifies the volume of outgoing network traffic to an application on a single instance.
	 * 
	 * Units: Bytes
	 * @return total compressed packets sent by all network devices
	 */
	public long getNetworkCompressedOut() {
		return tx_compressed;
	}

	
	/**
	 * MulticastIO:
	 * The number of bytes sent in and out over multicast on all network interfaces by the instance. This
	 * metric identifies the volume of multicast traffic to an application on a single instance.
	 * 
	 * Units: Bytes
	 * @return total bytes sent and received by all network devices
	 */
	public long getMulticastIO() {
		return multicast;
	}

	/**
	 * StatusCheckFailed:
	 * A combination of StatusCheckFailed_Instance and StatusCheckFailed_System that reports if either of
	 * the status checks has failed. Values for this metric are either 0 (zero) or 1 (one.) A zero indicates
	 * that the status check passed. A one indicates a status check failure.
	 * 
	 * Note: Status check metrics are available at 5 minute frequency and are not available in Detailed
	 *       Monitoring. For a newly launched instance, status check metric data will only be available
	 *       after the instance has completed the initialization state. Status check metrics will become
	 *       available within a few minutes of being in the running state.
	 * 
	 * Units: Count
	 * 
	 * @return 0 - currently not implemented
	 */
	public long getStatusCheckFailed() {
		return 0;
	}

	/**
	 * StatusCheckFailed_Instance:
	 * 
	 * Reports whether the instance has passed the EC2 instance status check in the last 5 minutes. Values
	 * for this metric are either 0 (zero) or 1 (one.) A zero indicates that the status check passed. A
	 * one indicates a status check failure.
	 * 
	 * Note: Status check metrics are available at 5 minute frequency and are not available in Detailed
	 *       Monitoring. For a newly launched instance, status check metric data will only be available
	 *       after the instance has completed the initialization state. Status check metrics will become
	 *       available within a few minutes of being in the running state.
	 * 
	 * Units: Count
	 * 
	 * @return 0 - for now
	 */
	public long getStatusCheckFailed_Instance() {
		return 0;
	}

	/**
	 * StatusCheckFailed_System:
	 * 
	 * Reports whether the instance has passed the EC2 system status check in the last 5 minutes. Values
	 * for this metric are either 0 (zero) or 1 (one.) A zero indicates that the status check passed. A
	 * one indicates a status check failure.
	 * 
	 * Note: Status check metrics are available at 5 minute frequency and are not available in Detailed 
	 *       Monitoring. For a newly launched instance, status check metric data will only be available
	 *       after the instance has completed the initialization state. Status check metrics will become
	 *       available within a few minutes of being in the running state.
	 * 
	 * Units: Count
	 * 
	 * @return 0 - for now
	 */
	public long getStatusCheckFailed_System() {
		return 0;
	}
}