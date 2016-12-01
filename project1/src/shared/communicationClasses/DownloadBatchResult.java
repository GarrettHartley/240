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
public class DownloadBatchResult {

	int batchID;
	int projectID;
	String imgURL;
	int firstYCoord;
	int recordHeight;
	int numberOfRecords;
	int numberOfFields;
	ArrayList<Field> fields;
	boolean valid;
	String urlPrefix;


	public DownloadBatchResult(){
		batchID = -1;
		projectID = -1;
		imgURL = new String();
		firstYCoord = -1;
		recordHeight = -1;
		numberOfRecords = -1;
		numberOfFields = -1;
		fields = new ArrayList<Field>();
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

	public int getProjectID() {
		return projectID;
	}

	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}

	public String getImgURL() {
		return imgURL;
	}

	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}

	public int getFirstYCoord() {
		return firstYCoord;
	}

	public void setFirstYCoord(int firstYCoord) {
		this.firstYCoord = firstYCoord;
	}

	public int getRecordHeight() {
		return recordHeight;
	}

	public void setRecordHeight(int recordHeight) {
		this.recordHeight = recordHeight;
	}

	public int getNumberOfRecords() {
		return numberOfRecords;
	}

	public void setNumberOfRecords(int numberOfRecords) {
		this.numberOfRecords = numberOfRecords;
	}

	public int getNumberOfFields() {
		return numberOfFields;
	}

	public void setNumberOfFields(int numberOfFields) {
		this.numberOfFields = numberOfFields;
	}

	public ArrayList<Field> getFields() {
		return fields;
	}

	public void setFields(ArrayList<Field> fields) {
		this.fields = fields;
	}
	
	public String getFullImgURL(){
		return urlPrefix+"/"+imgURL;
	}

	@Override
	public String toString() {
		if(valid==true){
			String result = new String();
			result=   batchID + "\n"
					+ projectID + "\n" 
					+ urlPrefix+"/"+imgURL + "\n"
					+ firstYCoord + "\n"
					+ recordHeight+"\n"
					+ numberOfRecords + "\n"
					+ numberOfFields + "\n";

			for(Field f:fields){
				result += f.getPrimaryID()+"\n"
						+ f.getFieldNumber()+"\n"
						+ f.getTitle()+"\n"
						+ urlPrefix+"/"+f.getHelpHTML()+"\n"
						+ f.getXcoord()+"\n"
						+ f.getWidth()+"\n";
				if(f.getKnownData()!= null){
					result +=  urlPrefix+"/"+f.getKnownData()+"\n";
				}
			}

			return result;
		}
		else{
			return "FAILED\n";
		}

	}




}
