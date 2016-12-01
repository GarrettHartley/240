package client.gui.indexerWindow.tableEntry;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import client.gui.BatchState;
import client.gui.BatchStateListener;


@SuppressWarnings("serial")
public class TableEntryModel extends AbstractTableModel implements BatchStateListener{

	BatchState batchState;
	ArrayList<ArrayList<TableEntryCell>> data;

	public TableEntryModel(BatchState batchState, ArrayList<ArrayList<TableEntryCell>> data) {
		this.data = batchState.getData();
		this.batchState = batchState;
		this.batchState.addUpdateDataListener(this);
	}

	@Override
	public int getColumnCount() {
		int returnValue = batchState.getNumberOfFields()+1;
		return batchState.getNumberOfFields()+1;
	}

	@Override
	public String getColumnName(int column) {
		column = column-1;
		if(column == -1){
			return "Records";
		}
		else if(batchState.isHasBatch()){
			return batchState.getFields().get(column).getTitle();
		}
		return "";
	}



	@Override
	public int getRowCount() {
		return batchState.getNumberOfRecords();
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		if(column == 0){
			return false;
		}
		return true;

	}

	@Override
	public Object getValueAt(int row, int column) {
		Object result = null;

		if (row >= 0 && row < getRowCount() && column >= 0
				&& column < getColumnCount()) {
			TableEntryCell cs = batchState.getData().get(row).get(column);
			result = cs;
		} else {
			throw new IndexOutOfBoundsException();
		}

		return result;
	}

	@Override
	public void setValueAt(Object value, int row, int column) {

		if (row >= 0 && row < getRowCount() && column >= 0
				&& column < getColumnCount()) {
			TableEntryCell cs = batchState.getData().get(row).get(column);
			cs.setValue((String)value);
			this.fireTableCellUpdated(row, column);
			batchState.updateData();
		} else {
			throw new IndexOutOfBoundsException();
		}		
	}

	@Override
	public void downloadBatch() {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectField() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateData() {
		//		System.out.println("TableEntryModel update Data:  "+data);
		//		this.data = batchState.getData();		
	}

	@Override
	public void updateSelected(TableEntryCell selectedCell) {
		// TODO Auto-generated method stub

	}

	@Override
	public void performLogout(String function) {
		// TODO Auto-generated method stub

	}


	@Override
	public void updateImage(String function) {
		// TODO Auto-generated method stub

	}

}