package shared.modelClasses;



import org.w3c.dom.Element;

import dataImporter.DataImporter;

/**
****************************************************<br>
				Database Key Help<br>
User: primaryID<br>
Project: primaryID<br>
Batch: primaryID, foreignProjectKey, foreignUserKey<br>
Field: primaryKey, foreignProjectKey<br>
Value: foreignBatchKey, foreignFieldKey<br>
*/
public class User {
	int primaryID;
	String username;
	String password;
	String firstname;
	String lastname;
	String email;
	int numRecords;
	
	public User(){
		primaryID = -1;
		username = new String();
		password = new String();
		firstname = new String();
		lastname = new String();
		email = new String();
		numRecords = -1;
	}
	
	public User(Element userElement ){
//		this.primaryID = user.getPrimaryID();
		username = DataImporter.getValue
				((Element)userElement.getElementsByTagName("username").item(0));
		password = DataImporter.getValue
				((Element)userElement.getElementsByTagName("password").item(0));
		firstname = DataImporter.getValue
				((Element)userElement.getElementsByTagName("firstname").item(0));
		lastname = DataImporter.getValue
				((Element)userElement.getElementsByTagName("lastname").item(0));
		email =  DataImporter.getValue
				((Element)userElement.getElementsByTagName("email").item(0));
		numRecords = Integer.parseInt(DataImporter.getValue
				((Element)userElement.getElementsByTagName("indexedrecords").item(0)));
		//Import it to the database here;
		
	}
	

	

	public int getNumRecords() {
		return numRecords;
	}

	public void setNumRecords(int numRecords) {
		this.numRecords = numRecords;
	}

	public int getPrimaryID() {
		return primaryID;
	}

	public void setPrimaryID(int primaryID) {
		this.primaryID = primaryID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
