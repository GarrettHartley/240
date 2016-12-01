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
public class Validate_user_param {

	String user;
	String password;
	
	public Validate_user_param(){
		user = new String();
		password = new String();
	}
	
	public Validate_user_param(String username, String password){
		user = username;
		this.password = password;
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
	
}
