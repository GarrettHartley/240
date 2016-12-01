package shared.modelClasses;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import dataImporter.DataImporter;

/**
 ****************************************************<br>
				Database Key Help<br>
User: primaryID<br>
Project: primaryID<br>
Batch: primaryID, foreignProjectKey, foreignUserKey<br>
Field: primaryKey, foreignProjectKey<br>
Value: foreignBatchKey, foreignFieldKey<br>
 */
public class Batch {

	/**
	 * BatchPrimaryID is unique for all batches regardless of their associated project
	 */
	int primaryID;
	int foreignProjectKey;
	int foreignUserKey;
	String imgURL;

	ArrayList<Value> values;

	public Batch(){
		primaryID =-1;
		foreignProjectKey = -1;
		foreignUserKey = -1;
		imgURL = new String();
	}

	public Batch(int primaryID, int foreignProjectKey, String imgURL){
		this.primaryID = primaryID;
		this.foreignProjectKey = foreignProjectKey;
		this.imgURL = imgURL;
	}



	public Batch(Element batchElement){
		imgURL = DataImporter.getValue((Element)batchElement.getElementsByTagName("file").item(0));
		values = new ArrayList<Value>();

		insertValues(batchElement);
	}

	public void insertValues(Element batchElement){

		NodeList valueElements = batchElement.getElementsByTagName("value");

		for(int i=0; i<valueElements.getLength();i++){
			values.add(new Value((Element)valueElements.item(i)));
		}
	}


	public ArrayList<Value> getValues() {
		return values;
	}

	public void setValues(ArrayList<Value> values) {
		this.values = values;
	}

	public int getPrimaryID() {
		return primaryID;
	}
	public void setPrimaryID(int primaryID) {
		this.primaryID = primaryID;
	}
	public int getForeignProjectKey() {
		return foreignProjectKey;
	}
	public void setForeignProjectKey(int foreignProjectKey) {
		this.foreignProjectKey = foreignProjectKey;
	}
	public int getForeignUserKey() {
		return foreignUserKey;
	}
	public void setForeignUserKey(int foreignUserKey) {
		this.foreignUserKey = foreignUserKey;
	}
	public String getImgURL() {
		return imgURL;
	}
	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}

}
