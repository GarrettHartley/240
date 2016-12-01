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
public class Submit_batch_result {
	
	boolean valid;
	
	public Submit_batch_result(){
		valid = false;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	@Override
	public String toString() {
		if(valid == true){
		return "TRUE\n";
		}
		else{
			return "FAILED\n";
		}
	}
	
}
