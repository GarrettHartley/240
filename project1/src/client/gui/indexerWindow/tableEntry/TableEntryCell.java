package client.gui.indexerWindow.tableEntry;

@SuppressWarnings("serial")
public class TableEntryCell{

	private String value = new String("");
	boolean correct = true;
	int row;
	int column;
	
	public TableEntryCell(String value,int row, int column) {
		setValue(value);
		this.row = row;
		this.column=column;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public String toString(){
		return value;
	}
	
	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}
	
}