package shared.communicationClasses;

import java.util.ArrayList;

import shared.modelClasses.Field;

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
public class GetFieldsResult {
	ArrayList<Field> fields;
	boolean valid;

	public GetFieldsResult(){
		fields = new ArrayList<Field>();
		valid = false;
	}

	public ArrayList<Field> getFields() {
		return fields;
	}

	public void setFields(ArrayList<Field> fields) {
		this.fields = fields;
	}

	public boolean isValid() {
		return false;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	@Override
	public String toString() {
		if(valid==true){
			String result = new String();
			for(Field f: fields){
				result+=(f.getForeignProjectKey()+"\n");
				result+=(f.getPrimaryID()+"\n");
				result+=(f.getTitle()+"\n");
			}
			return result;
		}
		else{
			return "FAILED\n";
		}
	}

}
