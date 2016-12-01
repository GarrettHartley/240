package client.gui.indexerWindow.formEntry;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.ListSelectionModel;

import client.gui.BatchState;
import client.gui.BatchStateListener;
import client.gui.indexerWindow.tableEntry.TableEntryCell;
import client.gui.indexerWindow.tableEntry.spellChecker.SpellCorrector;

@SuppressWarnings("serial")
public class FormEntry extends JPanel implements BatchStateListener {
	BatchState batchState;
	FormFields FormFields;
	ArrayList<FormFields> recordValues = new ArrayList<FormFields>();
	JPanel recordCards = new JPanel(new CardLayout());
	ArrayList<ArrayList<TableEntryCell>> data= new ArrayList<ArrayList<TableEntryCell>>();
	JList records;
	JScrollPane recordsScroll;
	JScrollPane recordCardsScroll;

	public FormEntry(BatchState batchState) {
		this.batchState = batchState;
		batchState.addUpdateDataListener(this);
		batchState.addUpdateDataListener(this);
		batchState.addUpdateSelectedListener(this);
		this.setLayout(new BorderLayout());
	}

	public FormEntry(BatchState batchState, boolean hasbatch) {
		this.batchState = batchState;
		batchState.addUpdateDataListener(this);
		batchState.addUpdateDataListener(this);
		batchState.addUpdateSelectedListener(this);
		this.setLayout(new BorderLayout());
		downloadBatch();
		resetValues();
	}

	public void resetValues(){
		for(int i=0; i<recordValues.size();i++){
			for(int j=1; j< recordValues.get(i).getValues().size()+1;j++){
				recordValues.get(i).getValues().get(j-1).setText(batchState.getData().get(i).get(j).getValue());
			}
		}
	}

	public FormEntry newFormEntry(){
		return this;
	}

	@Override
	public void downloadBatch() {
		DefaultListModel listModel = new DefaultListModel();
		records = new JList();
		records.addListSelectionListener(listSelectionListener);

		for(int i = 0; i<batchState.getNumberOfRecords(); i++){
			listModel.addElement(i+1);
			FormFields record = new FormFields(batchState,i);
			record.setRecordNumber(i);
			recordValues.add(record);
			record.setName(Integer.toString(i));
			recordCards.add(record,Integer.toString(i));
		}

		records = new JList(listModel);
		records.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		records.addListSelectionListener(listSelectionListener);
		recordsScroll = new JScrollPane(records,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		recordCardsScroll = new JScrollPane(recordCards,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		this.add(recordsScroll,BorderLayout.WEST);
		this.add(recordCardsScroll,BorderLayout.CENTER);

	}

	@Override
	public void selectField() {
		for (Component comp : recordCards.getComponents()) {
			if (comp.getName().equals(Integer.toString(batchState.getSelected().getRow()))) {
				((FormFields) comp).getValues().get(batchState.getSelected().getColumn()-1).grabFocus();
			}
		}
	}

	private ListSelectionListener listSelectionListener= new ListSelectionListener(){

		@Override
		public void valueChanged(ListSelectionEvent e) {
			if(!e.getValueIsAdjusting()){
				ArrayList<ArrayList<TableEntryCell>> newData= new ArrayList<ArrayList<TableEntryCell>>();			
				TableEntryCell tempCell = new TableEntryCell(batchState.getSelected().getValue(),Integer.parseInt(((JList)e.getSource()).getSelectedValue().toString())-1,batchState.getSelected().getColumn());
				batchState.updateSelected(tempCell);
			}
		}
	};


	@Override
	public void updateData() {
		for(int i = 0; i<recordValues.size();i++){
			for(int j=1; j<=recordValues.get(i).getValues().size();j++){
				recordValues.get(i).getValues().get(j-1).setText(batchState.getData().get(i).get(j).getValue());

				String fieldFilePath = batchState.getFields().get(batchState.getSelected().getColumn()-1).getKnownData();
				SpellCorrector currentDictionary = new SpellCorrector();
				try {
					currentDictionary.useDictionary(fieldFilePath);
				} catch (IOException e) {
					e.printStackTrace();
				}
				String inputWord = batchState.getData().get(i).get(j).getValue();
				if(currentDictionary.getDictionary().contains(inputWord)==false&&!inputWord.equals("")){
					System.out.println("Form Entry input word make me red "+inputWord);
					recordValues.get(i).getValues().get(j-1).setBackground(Color.red);
				}
			}
		}
	}


	@Override
	public void updateSelected(TableEntryCell selectedCell) {
		if(batchState.getSelected()!=null){
			records.setSelectedIndex(batchState.getSelected().getRow());
			CardLayout cl = (CardLayout)(recordCards.getLayout());
			cl.show(recordCards,Integer.toString(batchState.getSelected().getRow()));
			for (Component comp : recordCards.getComponents()) {
				if (comp.getName().equals(Integer.toString(batchState.getSelected().getRow()))) {
					if(batchState.getSelected().getColumn()!=0){
						((FormFields) comp).getValues().get(batchState.getSelected().getColumn()-1).grabFocus();
					}
				}
			}
			this.revalidate();
		}
	}

	public JPanel getRecordCards() {
		return recordCards;
	}

	public void setRecordCards(JPanel recordCards) {
		this.recordCards = recordCards;
	}

	public JScrollPane getRecordsScroll() {
		return recordsScroll;
	}

	public void setRecordsScroll(JScrollPane recordsScroll) {
		this.recordsScroll = recordsScroll;
	}

	public JScrollPane getRecordCardsScroll() {
		return recordCardsScroll;
	}

	public void setRecordCardsScroll(JScrollPane recordCardsScroll) {
		this.recordCardsScroll = recordCardsScroll;
	}
	
	@Override
	public void performLogout(String function) {

	}

	@Override
	public void updateImage(String function) {

	}




}
