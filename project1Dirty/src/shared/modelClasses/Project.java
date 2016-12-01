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
public class Project {
	int primaryID;
	String title;
	int recordsPerImage;
	int firstYCoord;
	int recordHeight;
	int numberOfFields;

	ArrayList<Field> fields;
	ArrayList<Batch> batches;

	public Project(){
		primaryID = -1;
		title = null;
		recordsPerImage=-1;
		firstYCoord=-1;
		recordHeight=-1;
		numberOfFields=-1;

	}

	public Project(Element projectElement){
		fields = new ArrayList<Field>();
		batches = new ArrayList<Batch>();

		title = DataImporter.getValue((Element)projectElement.getElementsByTagName("title").item(0));
		recordsPerImage = Integer.parseInt(DataImporter.getValue(
				(Element)projectElement.getElementsByTagName("recordsperimage").item(0)));
		firstYCoord = Integer.parseInt(DataImporter.getValue(
				(Element)projectElement.getElementsByTagName("firstycoord").item(0)));
		recordHeight = Integer.parseInt(DataImporter.getValue(
				(Element)projectElement.getElementsByTagName("recordheight").item(0)));
		
		insertFields(projectElement);
		insertBatches(projectElement);	

	}

	public void insertFields(Element projectElement){

		NodeList fieldElements =projectElement.getElementsByTagName("field");

		for(int i=0; i<fieldElements.getLength();i++){
			fields.add(new Field((Element)fieldElements.item(i)));
			numberOfFields++;
		}
	}

	public void insertBatches(Element projectElement){
		NodeList imageElements = projectElement.getElementsByTagName("image");

		for(int i=0; i < imageElements.getLength(); i++){
			batches.add(new Batch((Element)imageElements.item(i)));
		}
	}

	public ArrayList<Field> getFields() {
		return fields;
	}

	public void setFields(ArrayList<Field> fields) {
		this.fields = fields;
	}

	public ArrayList<Batch> getBatches() {
		return batches;
	}

	public void setBatches(ArrayList<Batch> batches) {
		this.batches = batches;
	}

	public int getPrimaryID() {
		return primaryID;
	}

	public void setPrimaryID(int primaryID) {
		this.primaryID = primaryID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getRecordsPerImage() {
		return recordsPerImage;
	}

	public void setRecordsPerImage(int recordsPerImage) {
		this.recordsPerImage = recordsPerImage;
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

	public int getNumberOfFields() {
		return numberOfFields;
	}

	public void setNumberOfFields(int numberOfFields) {
		this.numberOfFields = numberOfFields;
	}

}
