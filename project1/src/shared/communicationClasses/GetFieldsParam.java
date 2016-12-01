package shared.communicationClasses;

/**
this object is used to pass parameters of the server<br>
 ****************************************************<br>
				Database Key Help<br>
User: primaryID<br>
Project: primaryID<br>
Batch: primaryID, foreignProjectKey, foreignUserKey<br>
Field: primaryKey, foreignProjectKey<br>
Value: foreignBatchKey, foreignFieldKey<br>
 */
public class GetFieldsParam {

	String user;
	String password;
	int projectID;

	public GetFieldsParam(){
		user = new String();
		password = new String();
		projectID = -1;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getProjectID() {
		return projectID;
	}

	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}

}
