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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.StringTokenizer;

import au.edu.sydney.Curracurrong.datatype.DRecord;
import au.edu.sydney.Curracurrong.datatype.Data;

/**
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public class DBSink extends Sink {

	private Connection con;

	void initialize(int opID, Hashtable propertyMap) {
		// call constructor of super-class to initialize
		// channels
		super.initialize(opID, propertyMap);
		String connectionString = getPropertyValue("conn").tostring();
		String driver = "com.mysql.jdbc.Driver";
		String connection = connectionString;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(connection);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/* 
	 * 
	 */
	@Override
	public void write(Data d) {
		DRecord rec = (DRecord) d;
		if (rec != null) {

			// pass root and password as parameters along with connection string
			PreparedStatement statement = null;
			try {
				// List Of Columns - >
				// Sender,Receiver,ExecTime,UpTime,NumPackSent,NumByteSent,CPUUtil,DiskRead,DiskWrite,DiskReadByte,DiskWriteByte,NetworkIn,NetworkOut,statusCheckFailed,statusChkFailedInst,statusChkFailedSys
				statement = con
						.prepareStatement("insert into CCLOUD values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				statement.setString(1, rec.getElement(0).tostring());
				statement.setString(2, rec.getElement(1).tostring());
				for (int idx = 2; idx < rec.getSize(); idx++) {

					String recData = rec.getElement(idx).tostring();
					StringTokenizer token = new StringTokenizer(recData, ".");
					// System.out.println("Inx value -- >> "+idx+"  "+token+"  "+token.countTokens());
					if (token.countTokens() == 2) {
						statement.setDouble(idx + 1,
								Double.parseDouble(recData));
					} else {
						statement.setLong(idx + 1, Long.parseLong(recData));
					}
				}
				statement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
