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
public class SearchResult {

	boolean valid;
	int batchID;
	String imgURL;
	int recordNum;
	int fieldID;
	ArrayList<SearchResult> results;
	String urlPrefix;

	public SearchResult(){
		batchID = -1;
		imgURL = new String();
		recordNum = -1;
		fieldID = -1;
		results = new ArrayList<SearchResult>();
		valid = false;
		urlPrefix = new String();
	}

	public String getUrlPrefix() {
		return urlPrefix;
	}

	public void setUrlPrefix(String urlPrefix) {
		this.urlPrefix = urlPrefix;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public int getBatchID() {
		return batchID;
	}

	public void setBatchID(int batchID) {
		this.batchID = batchID;
	}

	public String getImgURL() {
		return imgURL;
	}

	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}

	public int getRecordNum() {
		return recordNum;
	}

	public void setRecordNum(int recordNum) {
		this.recordNum = recordNum;
	}

	public int getFieldID() {
		return fieldID;
	}

	public void setFieldID(int fieldID) {
		this.fieldID = fieldID;
	}

	public ArrayList<SearchResult> getResults() {
		return results;
	}

	public void setResults(ArrayList<SearchResult> results) {
		this.results = results;
	}

	@Override
	public String toString() {

		if(valid == true){
			String result = new String();
			for(SearchResult r: results){
				result += r.getBatchID() + "\n";
				result += urlPrefix+"/"+r.getImgURL()+"\n";
				result += r.getRecordNum()+"\n";
				result += r.getFieldID()+"\n";
			}
			return result;
		}
		else{
			return "FAILED\n";
		}
	}

}
