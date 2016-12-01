package shared.communicationClasses;

import java.util.ArrayList;

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
public class Get_projects_result {

	boolean valid;
	int projectID;
	String projectTitle;
	ArrayList<Get_projects_result> results;

	public ArrayList<Get_projects_result> getResults() {
		return results;
	}

	public void setResults(ArrayList<Get_projects_result> results) {
		this.results = results;
	}

	public Get_projects_result(){
		valid = false;
		projectID = -1;
		projectTitle = new String();
		results = new ArrayList<Get_projects_result>();
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public int getProjectID() {
		return projectID;
	}

	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}

	public String getProjectTitle() {
		return projectTitle;
	}

	public void setProjectTitle(String projectTitle) {
		this.projectTitle = projectTitle;
	}

	@Override
	public String toString() {
		if(valid==true){
		String result = new String();
		for(Get_projects_result r: results){
			result += r.getProjectID()+"\n"
					+ r.getProjectTitle() +"\n";
		}
		return result;
		}
		else{
			return "FAILED\n";
		}
		
		
	}

}
