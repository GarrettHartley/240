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
public class GetProjectsResult {

	boolean valid;
	int projectID;
	String projectTitle;
	ArrayList<GetProjectsResult> results;

	public ArrayList<GetProjectsResult> getResults() {
		return results;
	}

	public void setResults(ArrayList<GetProjectsResult> results) {
		this.results = results;
	}

	public GetProjectsResult(){
		valid = false;
		projectID = -1;
		projectTitle = new String();
		results = new ArrayList<GetProjectsResult>();
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
			for(GetProjectsResult r: results){
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
