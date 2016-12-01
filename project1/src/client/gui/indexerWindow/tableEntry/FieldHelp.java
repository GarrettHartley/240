package client.gui.indexerWindow.tableEntry;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JEditorPane;
import javax.swing.JTextField;

import client.gui.BatchState;
import client.gui.BatchStateListener;

public class FieldHelp extends JEditorPane implements BatchStateListener {

	BatchState batchState;

	public FieldHelp(BatchState batchState) {
		this.batchState = batchState;
	}

	@Override
	public void updateSelected(TableEntryCell selectedCell) {
		if(selectedCell!=null){  
			this.setContentType("text/html");
			try {
				if(selectedCell.getColumn()!=0){
					this.setPage(batchState.getUrlPrefix()+batchState.getFields().get(selectedCell.getColumn()-1).getHelpHTML());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
	public void performLogout(String function) {

	}

	@Override
	public void updateImage(String function) {

	}

}
