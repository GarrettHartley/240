package client.gui.indexerWindow.tableEntry;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;

import shared.modelClasses.Field;
import client.gui.BatchState;
import client.gui.BatchStateListener;
import client.gui.indexerWindow.tableEntry.spellChecker.SpellCorrector;

import java.io.IOException;
import java.util.*;

@SuppressWarnings("serial")
public class TableEntry extends JPanel implements BatchStateListener {

	private TableEntryModel tableModel;
	private JTable table = new JTable();
	BatchState batchState;
	ArrayList<ArrayList<TableEntryCell>> data = new ArrayList<ArrayList<TableEntryCell>>();

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	public TableEntry(BatchState batchState) throws HeadlessException {
		this.batchState = batchState;
		if(batchState.isHasBatch()){
			tableModel = new TableEntryModel(batchState, data);
			batchState.addUpdateDataListener(tableModel);
			batchState.addUpdateSelectedListener(this);
			table.setModel(tableModel);
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.setCellSelectionEnabled(true);
			table.getTableHeader().setReorderingAllowed(false);
			table.setMaximumSize(new Dimension(50,100));
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			TableColumnModel columnModel = table.getColumnModel();		
			for (int i = 0; i < tableModel.getColumnCount(); ++i) {
				TableColumn column = columnModel.getColumn(i);
				column.setPreferredWidth(50);
			}

			ArrayList<Field> fields = batchState.getFields();
			for (int i = 0; i < tableModel.getColumnCount(); ++i) {
				TableColumn column = columnModel.getColumn(i);
				int dataColumnIndex = i-1;
				if(dataColumnIndex==-1){
					column.setHeaderValue("Record");
				}
				else{
					column.setHeaderValue(fields.get(dataColumnIndex).getTitle());
				}
				ValueCellRenderer valueCellRenderer = new ValueCellRenderer();
				valueCellRenderer.setBatchState(batchState);
				column.setCellRenderer(valueCellRenderer);
			}


			JPanel rootPanel = new JPanel(new BorderLayout());
			rootPanel.add(table.getTableHeader(), BorderLayout.NORTH);
			rootPanel.add(table, BorderLayout.CENTER);

			this.add(rootPanel);

		}
	}

	public TableEntry(BatchState batchState, boolean dataSaved) throws HeadlessException {
		this.batchState = batchState;
		tableModel = new TableEntryModel(batchState, batchState.getData());
		batchState.addUpdateDataListener(tableModel);
		batchState.addUpdateSelectedListener(this);
		table.setModel(tableModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setCellSelectionEnabled(true);
		table.getTableHeader().setReorderingAllowed(false);
		table.setMaximumSize(new Dimension(50,100));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumnModel columnModel = table.getColumnModel();		
		for (int i = 0; i < tableModel.getColumnCount(); ++i) {
			TableColumn column = columnModel.getColumn(i);
			column.setPreferredWidth(50);
		}

		ArrayList<Field> fields = batchState.getFields();
		for (int i = 0; i < tableModel.getColumnCount(); ++i) {
			TableColumn column = columnModel.getColumn(i);
			int dataColumnIndex = i-1;
			if(dataColumnIndex==-1){
				column.setHeaderValue("Record");
			}
			else{
				column.setHeaderValue(fields.get(dataColumnIndex).getTitle());
			}
			ValueCellRenderer valueCellRenderer = new ValueCellRenderer();
			valueCellRenderer.setBatchState(batchState);
			column.setCellRenderer(valueCellRenderer);
		}

		JPanel rootPanel = new JPanel(new BorderLayout());
		rootPanel.add(table.getTableHeader(), BorderLayout.NORTH);
		rootPanel.add(table, BorderLayout.CENTER);
		this.add(rootPanel);
		table.setColumnSelectionInterval(0,1);
		table.setRowSelectionInterval(0,0);

	}

	@Override
	public void downloadBatch() {
		if(batchState.isHasBatch()){
			tableModel = new TableEntryModel(batchState, data);
			tableModel.addTableModelListener(tableModelListener);
			batchState.addUpdateDataListener(tableModel);
			table.setModel(tableModel);
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.setCellSelectionEnabled(true);
			table.getTableHeader().setReorderingAllowed(false);
			table.setMaximumSize(new Dimension(100,200));
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			TableColumnModel columnModel = table.getColumnModel();		
			for (int i = 0; i < tableModel.getColumnCount(); ++i) {
				TableColumn column = columnModel.getColumn(i);
				column.setPreferredWidth(50);
				column.setMaxWidth(50);
			}

			ArrayList<Field> fields = batchState.getFields();
			for (int i = 0; i < tableModel.getColumnCount(); ++i) {
				TableColumn column = columnModel.getColumn(i);
				int dataColumnIndex = i-1;
				if(dataColumnIndex==-1){
					column.setHeaderValue("Record");
				}
				else{
					column.setHeaderValue(fields.get(dataColumnIndex).getTitle());
				}

				column.setCellRenderer(new ValueCellRenderer());
				//				column.setCellEditor(new ValueCellEditor());
			}


			JPanel rootPanel = new JPanel(new BorderLayout());
			rootPanel.add(table.getTableHeader(), BorderLayout.NORTH);
			rootPanel.add(table, BorderLayout.CENTER);

			this.add(rootPanel);

		}

	}


	@Override
	public void updateData() {
		System.out.println("this is the value TableEnrtyCell updateData: ");
		this.data = data;
	}

	@Override
	public void updateSelected(TableEntryCell selectedCell) {
		if(selectedCell!=null){
			table.changeSelection(selectedCell.getRow(),selectedCell.getColumn(),false, false);
		}
	}

	private TableModelListener tableModelListener = new TableModelListener(){
		@Override
		public void tableChanged(TableModelEvent e) {
			System.out.println("tableChanged");
		}

	};

	@Override
	public void performLogout(String function) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateImage(String function) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void selectField() {

	}



}


@SuppressWarnings("serial")
class ValueCellRenderer extends JLabel implements TableCellRenderer {

	private Border unselectedBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
	private Border selectedBorder = BorderFactory.createLineBorder(Color.green, 2);
	BatchState  batchState = new BatchState();

	public void setBatchState(BatchState batchState){
		this.batchState = batchState;	
	}

	public ValueCellRenderer() {
		setFont(getFont().deriveFont(16.0f));
	}

	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column) {

		if (isSelected) {
			this.setBorder(selectedBorder);
			TableEntryCell tempCell = new TableEntryCell("",row, column);
			batchState.updateSelected(tempCell);
		}
		else {
			this.setBorder(unselectedBorder);
		}

		this.setText(value.toString());

		
		if(false==this.getText().equals("")&& batchState.getSelected().getColumn()!=0){
			System.out.println("what the heck   "+(batchState.getSelected().getColumn()-1));
			String fieldFilePath = batchState.getFields().get(batchState.getSelected().getColumn()-1).getKnownData();
//			System.out.println(batchState.getFields().get(0).getKnownData());
						SpellCorrector currentDictionary = new SpellCorrector();
			try {
				currentDictionary.useDictionary(fieldFilePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String inputWord = value.toString();

			if(inputWord!="" &&!inputWord.matches(".*[0-9].*") ){
				inputWord = inputWord.toLowerCase();
				if(currentDictionary.getDictionary().contains(inputWord)==false ){
					this.setBackground(Color.red);
					this.setOpaque(true);
				}
			}

		}		
		else{
			this.setBackground(Color.white);
		}



		return this;
	}

}



