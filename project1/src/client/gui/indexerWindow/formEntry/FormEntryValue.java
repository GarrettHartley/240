package client.gui.indexerWindow.formEntry;

import javax.swing.JTextField;

public class FormEntryValue extends JTextField {
	int recordNumber = -1;
	int fieldNumber = -1;
	
	public int getRecordNumber() {
		return recordNumber;
	}
	public void setRecordNumber(int recordNumber) {
		this.recordNumber = recordNumber;
	}
	public int getFieldNumber() {
		return fieldNumber;
	}
	public void setFieldNumber(int fieldNumber) {
		this.fieldNumber = fieldNumber;
	}


}
