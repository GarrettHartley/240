package client.gui.indexerWindow.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JToolBar;

import client.gui.BatchState;
import client.gui.BatchStateListener;
import client.gui.indexerWindow.tableEntry.TableEntryCell;

public class ButtonMenu extends JToolBar implements BatchStateListener {
	BatchState batchState;
	JButton zoomIn = new JButton("Zoom In");
	JButton zoomOut = new JButton("Zoom Out");
	JButton invertImage = new JButton("Invert Image");
	JButton toggleHighlights = new JButton("Toggle Highlights");		
	JButton save = new JButton("Save");		
	JButton submit = new JButton("Submit");


	public ButtonMenu(BatchState batchState) {
		this.batchState = batchState;
		invertImage.addActionListener(al);
		zoomIn.addActionListener(al);
		zoomOut.addActionListener(al);
		toggleHighlights.addActionListener(al);
		submit.addActionListener(al);
		save.addActionListener(saveAction);
		this.add(zoomIn);
		this.add(zoomOut);
		this.add(invertImage);
		this.add(toggleHighlights);
		this.add(save);
		this.add(submit);
	}


	private ActionListener al = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			Object o = e.getSource();
			JButton button = null;
			if(o instanceof JButton){
				button = (JButton)o;
			}
			System.out.println("This is actionPerformed: "+button.getText());
			batchState.updateImage(button.getText());
		}

	};

	private ActionListener saveAction = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {

			Object o = e.getSource();
			JButton button = null;
			if(o instanceof JButton){
				button = (JButton)o;
			}
			System.out.println("This is actionPerformed: "+button.getText());
			batchState.logout(button.getText());
		}



	};

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
