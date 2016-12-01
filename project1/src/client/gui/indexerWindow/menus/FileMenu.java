package client.gui.indexerWindow.menus;

import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import client.ClientFacade;
import client.gui.BatchState;
import client.gui.BatchStateListener;
import client.gui.indexerWindow.tableEntry.TableEntryCell;

@SuppressWarnings("serial")
public class FileMenu extends JMenuBar implements BatchStateListener {
	JMenu menu = new JMenu("File");
	JMenuItem downloadBatch = new JMenuItem("Download Batch");
	JMenuItem logout = new JMenuItem("Logout");
	JMenuItem exit = new JMenuItem("Exit");	
	static BatchState batchState;
	static ClientFacade clientFacade;
	public FileMenu(BatchState batchState,ClientFacade clientFacade) {
		this.clientFacade=clientFacade;
		this.batchState = batchState;
		menu.add(downloadBatch);
		menu.add(logout);
		menu.add(exit);	
		this.add(menu);
		downloadBatch.addActionListener(downloadBatchListener);
		logout.addActionListener(logoutListener);
		exit.addActionListener(logoutListener);
	}
	
	private static ActionListener logoutListener = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Logout listener");
			Object o = e.getSource();
			JMenuItem menuItem = null;
			if(o instanceof JMenuItem){
				menuItem = (JMenuItem)o;
			}
			System.out.println("This is actionPerformed: "+menuItem.getText());
				batchState.logout(menuItem.getText());
		}
	};
	
	private static ActionListener downloadBatchListener = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			DownloadBatchWindow downloadBatchWindow = new DownloadBatchWindow(batchState,clientFacade);
			downloadBatchWindow.setModalityType(ModalityType.APPLICATION_MODAL);

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
