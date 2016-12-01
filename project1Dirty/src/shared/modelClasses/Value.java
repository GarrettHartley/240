package shared.modelClasses;

import org.w3c.dom.Element;

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
public class Value {
	
	int foreignImageKey;
	int foreignFieldKey;
	
	public int getForeignFieldKey() {
		return foreignFieldKey;
	}

	public void setForeignFieldKey(int foreignFieldKey) {
		this.foreignFieldKey = foreignFieldKey;
	}


	int col;
	/**
	 * defines the row within the batch of the cell
	 */
	int row;
	/**
	 * contains the value of the cell in the batch
	 */
	String value;
	

	public Value(){
		foreignImageKey=-1;
		foreignFieldKey=-1;
		col=-1;
		row=-1;
		value=new String();
	}
	
	public void setValue(int foreignImageKey, int foreignFieldKey, int col, int row, String value){
		this.foreignImageKey=foreignImageKey;
		this.foreignFieldKey=foreignFieldKey;
		this.col=col;
		this.row=row;
		this.value=value;
	}
	
	public Value(Element ValueElement){
		// I dont't know how I'm gonna do keys
		//System.out.println(DataImporter.getValue(ValueElement));
		value = DataImporter.getValue(ValueElement);
	}


	public int getForeignImageKey() {
		return foreignImageKey;
	}


	public void setForeignImageKey(int foreignBatchKey) {
		this.foreignImageKey = foreignBatchKey;
	}


	public int getCol() {
		return col;
	}


	public void setCol(int col) {
		this.col = col;
	}


	public int getRow() {
		return row;
	}


	public void setRow(int row) {
		this.row = row;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}
	
	
	
}
