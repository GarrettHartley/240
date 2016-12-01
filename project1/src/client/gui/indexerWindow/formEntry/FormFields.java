package client.gui.indexerWindow.formEntry;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import shared.modelClasses.Field;
import client.gui.BatchState;
import client.gui.BatchStateListener;
import client.gui.indexerWindow.tableEntry.TableEntryCell;
import client.gui.indexerWindow.tableEntry.spellChecker.SpellCorrector;

/**
 * @author hartley9
 *
 */
@SuppressWarnings("serial")
public class FormFields extends JPanel implements BatchStateListener{

	BatchState batchState;
	ArrayList<JTextField> values = new ArrayList<JTextField>();
	int recordNumber = -1;

	public FormFields(BatchState batchState, int recordNumber){
		this.batchState = batchState;
		if(batchState.isHasBatch()){
			this.setPreferredSize(new Dimension(100,100));
			batchState.addUpdateSelectedListener(this);
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			int i = 0;
			for(Field field: batchState.getFields()){
				i+=1;
				JPanel fieldAndValue = new JPanel();
				fieldAndValue.setLayout(new BoxLayout(fieldAndValue, BoxLayout.X_AXIS));
				JLabel fieldTitle = new JLabel(field.getTitle());
				FormEntryValue value = new FormEntryValue();
				value.setRecordNumber(recordNumber);
				value.setFieldNumber(i);
				value.addFocusListener(focusListener);
				value.addActionListener(actionListener);
				value.setMaximumSize(new Dimension(100,20));
				value.getDocument().addDocumentListener(documentListener);
				fieldAndValue.add(fieldTitle);
				fieldAndValue.add(value);
				this.add(fieldAndValue);
				values.add(value);
			}
		}
	}
	
	public int getRecordNumber() {
		return recordNumber;
	}

	public void setRecordNumber(int recordNumber) {
		this.recordNumber = recordNumber;
	}

	private DocumentListener documentListener = new DocumentListener(){

		@Override
		public void insertUpdate(DocumentEvent e) {
			changedUpdate(e);
		}
		@Override
		public void removeUpdate(DocumentEvent e) {
			changedUpdate(e);
		}
		@Override
		public void changedUpdate(DocumentEvent e) {
			String value = new String();
			try {
				value = e.getDocument().getText(0, e.getDocument().getLength());
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
			
			if(batchState.isFormEntrySelected()){
			TableEntryCell temp = batchState.getSelected();
			batchState.getData().get(temp.getRow()).get(temp.getColumn()).setValue(value);
			}
		}
		
	};
	
	private FocusListener focusListener = new FocusListener(){

		@Override
		public void focusGained(FocusEvent e) {
			batchState.updateSelected(new TableEntryCell("",((FormEntryValue)e.getSource()).getRecordNumber(),((FormEntryValue)e.getSource()).getFieldNumber()));
		}

		@Override
		public void focusLost(FocusEvent e) {
			
			String fieldFilePath = batchState.getFields().get(batchState.getSelected().getColumn()-1).getKnownData();
			SpellCorrector currentDictionary = new SpellCorrector();
			try {
				currentDictionary.useDictionary(fieldFilePath);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			String inputWord =((FormEntryValue) e.getSource()).getText();
			
			if(currentDictionary.getDictionary().contains(inputWord)==false&&!inputWord.equals("")){
				((Component) e.getSource()).setBackground(Color.red);
			}
			else{
				((FormEntryValue) e.getSource()).setBackground(Color.white);
			}
		}
	};

	private ActionListener actionListener = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() instanceof JTextField){
				((JTextField) e.getSource()).setText("hello");
			}
		}
	};

	public void setValues(ArrayList<JTextField> values) {
		this.values = values;
	}

	public ArrayList<JTextField> getValues(){
		return values;
	}
	
	@Override
	public void downloadBatch() {
		
	}

	@Override
	public void selectField() {

	}

	@Override
	public void updateData() {
		
	}

	@Override
	public void updateSelected(TableEntryCell selectedCell) {

	}

	@Override
	public void performLogout(String function) {

		
	}

	@Override
	public void updateImage(String function) {

		
	}


}
