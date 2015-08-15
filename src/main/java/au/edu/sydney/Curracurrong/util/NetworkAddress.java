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

package au.edu.sydney.Curracurrong.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

/**
 * Utility class to deal with network addresses.
 * 
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public class NetworkAddress {
	public static  InetAddress getAddress() throws Exception {
		Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
		for (NetworkInterface netint : Collections.list(nets)) {
			try {
				if (!netint.isUp()) {
					// System.out.println("Interface not up [" + netint.getName() + "]");
					continue;
				}
			} catch (SocketException e) {
				System.out.println("Unable to check network interface status");
				e.printStackTrace();
				continue;
			}
			String name = netint.getName();
//			System.out.println("Interface name: [" + name + "]");
			if (name.startsWith("eth") || name.startsWith("en")) {
				// get the first non-loopback ethernet interface that is up (lo)
				// this needs to be improved
				for (InetAddress addr : Collections.list(netint.getInetAddresses())) {
					// System.out.println("Network interfaces [" + name + "] checking for IPv4 address [" + addr + "]");
					if (addr instanceof Inet4Address)
						return addr;
				}
			}
			// System.out.println("Network interfaces [" + name + "] up but has no IPv4 address!!!");
		}
		throw new Exception("Unable to fetch network address");
	}
	public static void main(String[] args) {
		try {
			System.out.println("My address is: [" + NetworkAddress.getAddress().getHostAddress() + "]");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
