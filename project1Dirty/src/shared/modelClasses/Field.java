package shared.modelClasses;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import dataImporter.DataImporter;

/**
 * 
 * FIELD = COLUMN
****************************************************<br>
				Database Key Help<br>
User: primaryID<br>
Project: primaryID<br>
Batch: primaryID, foreignProjectKey, foreignUserKey<br>
Field: primaryKey, foreignProjectKey<br>
Value: foreignBatchKey, foreignFieldKey<br>
*/
public class Field {
	/**
	 * Primary field IDs are ALL unique regardless of project
	 */
	int primaryID;
	/**
	 * Specifies the physical position of the field on the images for its project
	 */
	int fieldNumber;
	int foreignProjectKey;
	String title;
	int xcoord;
	int width;
	String helpHTML;
	String knownData;
	
	ArrayList<Value> values;
	
	public Field(){
		primaryID = -1;
		foreignProjectKey = -1;
		fieldNumber = -1;
		
		title = new String();
		xcoord = -1;
		width = -1;
		helpHTML = new String();
		knownData= new String();
		
		values = new ArrayList<Value>();
	}
	
	public Field(Element fieldElement){		
		title = DataImporter.getValue
				((Element)fieldElement.getElementsByTagName("title").item(0));
		xcoord = Integer.parseInt(DataImporter.getValue
				((Element)fieldElement.getElementsByTagName("xcoord").item(0)));
		width = Integer.parseInt(DataImporter.getValue
				((Element)fieldElement.getElementsByTagName("width").item(0)));
		helpHTML = DataImporter.getValue
				((Element)fieldElement.getElementsByTagName("helphtml").item(0));
		
		if(fieldElement.getElementsByTagName("knowndata").getLength()!=0){
			knownData = DataImporter.getValue
					((Element)fieldElement.getElementsByTagName("knowndata").item(0));
		}
		
	}


	public int getFieldNumber() {
		return fieldNumber;
	}

	public void setFieldNumber(int fieldNumber) {
		this.fieldNumber = fieldNumber;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getXcoord() {
		return xcoord;
	}

	public void setXcoord(int xcoord) {
		this.xcoord = xcoord;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getHelpHTML() {
		return helpHTML;
	}

	public void setHelpHTML(String helpHTML) {
		this.helpHTML = helpHTML;
	}

	public String getKnownData() {
		return knownData;
	}

	public void setKnownData(String knownData) {
		this.knownData = knownData;
	}

}
