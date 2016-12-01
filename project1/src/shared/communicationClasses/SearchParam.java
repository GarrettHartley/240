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
public class SearchParam {

	String user;
	String password;
	ArrayList<Integer> fieldsToSearch;
	ArrayList<String> searchValues;
	String urlPrefix;

	public SearchParam(){
		user = new String();
		password = new String();
		fieldsToSearch = new ArrayList<Integer>();
		searchValues = new ArrayList<String>();
		urlPrefix = new String();
	}

	public String getUrlPrefix() {
		return urlPrefix;
	}

	public void setUrlPrefix(String urlPrefix) {
		this.urlPrefix = urlPrefix;
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

	public ArrayList<Integer> getFieldsToSearch() {
		return fieldsToSearch;
	}

	public void setFieldsToSearch(String inputString) {
		ArrayList<Integer> fieldsToSearch = new ArrayList<Integer>();
		String[] fields = inputString.split(",",-1);
		for(String str: fields){
			fieldsToSearch.add(Integer.parseInt(str));
		}
		this.fieldsToSearch = fieldsToSearch;
	}

	public ArrayList<String> getSearchValues() {
		return searchValues;
	}

	public void setSearchValues(String inputString) {
		ArrayList<String> searchValues = new ArrayList<String>();
		String[] values = inputString.split(",",-1);
		for(String str: values){
			searchValues.add(str);
		}
		this.searchValues = searchValues;
	}
}
