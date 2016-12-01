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
public class Validate_user_result {

	Boolean valid;
	String firstname;
	String lastname;
	int numberOfRecords;
	String output;

	public Validate_user_result(){
		valid = false;
		firstname = new String();
		output = new String("FAILED");
		lastname = new String();
		int numberOfRecords = -1;
	}

	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
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

	public int getNumberOfRecords() {
		return numberOfRecords;
	}

	public void setNumberOfRecords(int numberOfRecords) {
		this.numberOfRecords = numberOfRecords;
	}

	@Override
	public String toString() {
		if(valid == true){
		return  valid + "\n"
				+ firstname + "\n"
				+ lastname + "\n"
				+ numberOfRecords;
		}
		else{
			return output+"\n";
		}
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

}
