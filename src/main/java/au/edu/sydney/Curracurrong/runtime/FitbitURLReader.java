package au.edu.sydney.Curracurrong.runtime;


import java.net.URL;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Vector;

import java.net.*;
import java.io.*;

import org.json.JSONObject;



import au.edu.sydney.Curracurrong.datatype.DFloat;
import au.edu.sydney.Curracurrong.datatype.DInteger32;
import au.edu.sydney.Curracurrong.datatype.DInteger64;
import au.edu.sydney.Curracurrong.datatype.DRecord;
import au.edu.sydney.Curracurrong.datatype.DString;
import au.edu.sydney.Curracurrong.datatype.ParsingException;
import au.edu.sydney.Curracurrong.util.NetworkAddress;

public class FitbitURLReader extends Sensor {
	
	 private DRecord sense;
	
	void initialize(int opID, Hashtable propertyMap) {
        super.initialize(opID, propertyMap);
	}
	
	public DRecord read() {
		senseAll();
		return sense;
		
	}
	
	/*private void senseAll() {
		 sense = new DRecord(7);
		 try{
			URL url = new URL("http://jsonplaceholder.typicode.com/users/1/todos");
		    BufferedReader in = new BufferedReader(
		        new InputStreamReader(url.openStream()));
		    sense.setElement(0, new DFloat(SystemStatus.getSystemStatusInstance().getExecutionTime()));
	        sense.setElement(1, new DInteger64(System.currentTimeMillis()));
	        sense.setElement(2, new DInteger64(SystemStatus.getSystemStatusInstance().getNumPacketsSent()));
	
	        StringBuffer inputLine = new StringBuffer();
	        String input;
	        input = in.readLine();
       	 	Vector<StringBuffer> inputArray = new Vector<StringBuffer>();
       	 	while ((input = in.readLine()) != null) {
       	 		if(input.trim().equals("{")) {
       	 			inputLine = new StringBuffer();
       	 		}
                inputLine.append(input.trim());
                if(input.trim().contains("}")) {
                	inputArray.add(inputLine);
                }
       	 	}
	       
	        
	       
       	 	String convertedString = inputArray.get(0).toString();
	        JSONObject jobj = new JSONObject(convertedString);
	        
        	sense.setElement(3, new DInteger32(jobj.getInt("userId")));
        	sense.setElement(4, new DString(jobj.getString("title").toString()));
            sense.setElement(5, new DInteger32(jobj.getInt("id")));
            sense.setElement(6, new DString(String.valueOf(jobj.getBoolean("completed"))));
	        in.close();
		 } catch(IOException ex){
			 ex.printStackTrace();
		 } catch(ParsingException ex) {
			 ex.printStackTrace();
		 }
	 }*/
	
	/*public void senseAll() {
		sense = new DRecord(7);
		String ourAddress = null;
		try {
			ourAddress = NetworkAddress.getAddress().getHostAddress();
		} catch (Exception e) {
            System.out.println("CASCADE: unable to fetch information about network interfaces");
		}
		Runtime run = Runtime.getRuntime();
		byte[] data = new byte[256];
		String receiveFrom = "ec2-user@" + "10.0.0.12";
		String command1 = "ssh -i vasvi-ec2-curracurrong.pem " + receiveFrom + " less ~/SampleJASON" + ourAddress;
		System.out.println("command -->" + command1);
		try {
			Process p = run.exec(command1);
			InputStream streamReader =  p.getInputStream();
			
			String jsonstring = readFully(streamReader, "UTF-8");
			System.out.println("string received from json " + jsonstring);
			
			int exit = p.waitFor();
			
			sense.setElement(0, new DFloat(SystemStatus.getSystemStatusInstance().getExecutionTime()));
	        sense.setElement(1, new DInteger64(System.currentTimeMillis()));
	        sense.setElement(2, new DInteger64(SystemStatus.getSystemStatusInstance().getNumPacketsSent()));
	        
	        /*Vector<Integer> indexArray = findIndex(jsonstring);
	        StringBuffer currentLine = new StringBuffer(jsonstring);
	        for(int index=0; index<indexArray.size(); index++) {
	        	currentLine.insert(indexArray.get(index)+index, "\\");
	        }
	        String convertedString = currentLine.toString();
	        System.out.println("Converted String " + convertedString);*
	        org.json.JSONObject jobj = new org.json.JSONObject(jsonstring);
	        
        	sense.setElement(3, new DInteger32(jobj.getInt("userId")));
        	sense.setElement(4, new DString(jobj.getString("title").toString()));
            sense.setElement(5, new DInteger32(jobj.getInt("id")));
            sense.setElement(6, new DString(String.valueOf(jobj.getBoolean("completed"))));
	        
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}*/
	
	public void senseAll() {
		sense = new DRecord(7);
		String ourAddress = null;
		try {
			ourAddress = NetworkAddress.getAddress().getHostAddress();
		} catch (Exception e) {
            System.out.println("CASCADE: unable to fetch information about network interfaces");
		}
		Runtime run = Runtime.getRuntime();
		byte[] data = new byte[256];
		String receiveFrom = "ec2-user@" + "10.0.0.12";
		String command1 = "scp -i vasvi-ec2-curracurrong.pem " + receiveFrom +":~/SampleJASON" + ourAddress + " /tmp/";
		File file = new File("/tmp/SampleJASON" + ourAddress);
		
		System.out.println("command -->" + command1);
		try {

			Scanner scan = new Scanner(file);
			String jsonstring = null;
			while(scan.hasNextLine()) {
				jsonstring = scan.nextLine();
			}
			System.out.println("string received from json " + jsonstring);
			
			
			sense.setElement(0, new DFloat(SystemStatus.getSystemStatusInstance().getExecutionTime()));
	        sense.setElement(1, new DInteger64(System.currentTimeMillis()));
	        sense.setElement(2, new DInteger64(SystemStatus.getSystemStatusInstance().getNumPacketsSent()));
	        
	        
	        org.json.JSONObject jobj = new org.json.JSONObject(jsonstring);
	        
        	sense.setElement(3, new DInteger32(jobj.getInt("userId")));
        	sense.setElement(4, new DString(jobj.getString("title").toString()));
            sense.setElement(5, new DInteger32(jobj.getInt("id")));
            sense.setElement(6, new DString(String.valueOf(jobj.getBoolean("completed"))));
	        
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
	public String readFully(InputStream inputStream, String encoding)
	        throws IOException {
	    return new String(readFully(inputStream), encoding);
	}    

	private byte[] readFully(InputStream inputStream)
	        throws IOException {
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    byte[] buffer = new byte[1024];
	    int length = 0;
	    while ((length = inputStream.read(buffer)) != -1) {
	        baos.write(buffer, 0, length);
	    }
	    return baos.toByteArray();
	}
	
	 
	 private Vector<Integer> findIndex(String line) {
    	Vector<Integer> indexVector = new Vector<Integer>();
    	for(int i=0; i < line.length(); i++) {
    		if(line.charAt(i) == '\"') {
    			indexVector.add(i);
    		}
    	}
    	return indexVector;
    }
}
