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

package au.edu.sydney.Curracurrong.runtime.server;

import au.edu.sydney.Curracurrong.datatype.ParsingException;

import java.io.*;
import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

import au.edu.sydney.Curracurrong.queryLanguage.TLexer;
import au.edu.sydney.Curracurrong.queryLanguage.TParser;
import au.edu.sydney.Curracurrong.runtime.Registry;
import au.edu.sydney.Curracurrong.scheduler.Scheduler;
import au.edu.sydney.Curracurrong.util.NetworkAddress;

/**
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public class CurracurrongServer {
	protected ServerAdministrator admin;
	protected ServerCommunicator comm;
	protected Scheduler sched;
	
	protected String inputStream = "src/queryLanguage/test1";

	public CurracurrongServer() {
	}
	
	public CurracurrongServer(String streamName) {
		inputStream = streamName;
	}
	
	/**
	 * Print out our radio address.
	 */
	public void run() throws IOException, RecognitionException, ParsingException {
		String ourAddress = null;
		try {
			ourAddress = NetworkAddress.getAddress().getHostAddress();
		} catch (Exception e) {
            System.out.println("CASCADE: unable to fetch information about network interfaces");
		}
		System.out.println("Our address = " + ourAddress);

		System.out.println("start...");

		// create scheduler
		sched = Scheduler.getSchedulerInstance();
		System.out.println("new scheduler " + sched.toString());

		// create communicator
		comm = ServerCommunicator.getCommunicatorInstance();
		System.out.println("neww communicator " + comm.toString());

		// create administartor
		admin = ServerAdministrator.getAdministratorInstance();
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
		// run scheduler
		// sched.start();  // this is now done in Administrator.run() after receiving a "commit" message
		System.out.println("postpone scheduler start ...");

		// CharStream input = new ANTLRFileStream("C:\\query\\test7.txt");
		CharStream input = new ANTLRFileStream(inputStream);
		// ANTLRInputStream input = new ANTLRInputStream(System.in);
		TLexer lex = new TLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lex);
		// System.out.println("tokens="+tokens);
		TParser parser = new TParser(tokens);
		parser.program();

		// System.exit(0);
	}

	/**
	 * Start up the host application.
	 * 
	 * @param args
	 *            any command line arguments
	 */
	public static void main(String[] args) throws IOException,
			RecognitionException, ParsingException {
		CurracurrongServer app = null;
		if (args.length > 1 && args[1] != null) {
			System.out.println("Loading query file: [" + args[1] + "]");
			app = new CurracurrongServer(args[1]);
		} else {
			app = new CurracurrongServer();			
		}
		app.run();
	}
}