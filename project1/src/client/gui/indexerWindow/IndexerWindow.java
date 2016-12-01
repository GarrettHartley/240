package client.gui.indexerWindow;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import client.ClientFacade;
import client.gui.BatchState;
import client.gui.BatchStateListener;
import client.gui.indexerWindow.dragableImagePanel.DraggingComponent;
import client.gui.indexerWindow.formEntry.FormEntry;
import client.gui.indexerWindow.menus.ButtonMenu;
import client.gui.indexerWindow.menus.FileMenu;
import client.gui.indexerWindow.tableEntry.FieldHelp;
import client.gui.indexerWindow.tableEntry.TableEntry;
import client.gui.indexerWindow.tableEntry.TableEntryCell;

public class IndexerWindow extends JFrame implements BatchStateListener {

	FileMenu fileMenu;
	ButtonMenu buttonMenu;
	TableEntry tableEntry;
	FormEntry formEntry;
	FieldHelp fieldHelp;
	ClientFacade clientFacade;
	DraggingComponent dragImageFrame = new DraggingComponent();
	BatchState batchState;
	JTabbedPane tabsBottomLeft = new JTabbedPane();
	JSplitPane middleAndBottom;

	JScrollPane tableEntryScroll;
	JScrollPane bottomRight;
	JSplitPane bottom;


	public IndexerWindow(ClientFacade clientFacade){

		batchState = null;

		XStream xmlStream = new XStream(new DomDriver());
		File xmlFile = new File(clientFacade.getUser()+".xml");

		System.out.println("xmlFile exists: "+xmlFile.exists());

		if(xmlFile.exists()){
			batchState = (BatchState) xmlStream.fromXML(xmlFile);
			batchState.setDownloadBatchListeners(new ArrayList<BatchStateListener>());
			batchState.setSelectFieldListeners(new ArrayList<BatchStateListener>());
			batchState.setUpdateDataListeners(new ArrayList<BatchStateListener>());
			batchState.setUpdateSelectedListeners(new ArrayList<BatchStateListener>());
			batchState.setLogoutListeners(new ArrayList<BatchStateListener>());
			batchState.setUpdateImageListeners(new ArrayList<BatchStateListener>());
			System.out.println("this one existed");

			fileMenu = new FileMenu(batchState,clientFacade);
			buttonMenu = new ButtonMenu(batchState);
			tableEntry = new TableEntry(batchState,true);
			formEntry = new FormEntry(batchState, true);
			fieldHelp = new FieldHelp(batchState);

			dragImageFrame=new DraggingComponent(batchState,batchState.getImageYLoc() , batchState.getImageXLoc() );
		}

		else{
			batchState = new BatchState();
			fileMenu = new FileMenu(batchState,clientFacade);
			buttonMenu = new ButtonMenu(batchState);
			tableEntry = new TableEntry(batchState);
			formEntry = new FormEntry(batchState);
			fieldHelp = new FieldHelp(batchState);
			dragImageFrame.setSize(new Dimension(500,900));
		}

		this.clientFacade = clientFacade;
		batchState.addDownloadBatchListener(this);
		batchState.addUpdateImageListener(this);


		if(batchState.isHasBatch()==false){
			for(Component p: buttonMenu.getComponents()){
				((JButton)p).setEnabled(false);
			}
		}

		batchState.addDownloadBatchListener(formEntry);
		batchState.addUpdateDataListener(formEntry);
		batchState.addDownloadBatchListener(tableEntry);
		batchState.addUpdateDataListener(tableEntry);
		batchState.addUpdateSelectedListener(this);
		batchState.addUpdateSelectedListener(fieldHelp);
		//Window				
		this.setPreferredSize(new Dimension(1000,900));
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Top menus		
		JPanel top = new JPanel();
		top.setPreferredSize(new Dimension(100,30));
		top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
		top.add(fileMenu);
		top.add(buttonMenu);

		//Bottom left		
		JPanel panelBottomLeft = new JPanel();

		tableEntryScroll = new JScrollPane(tableEntry,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		tabsBottomLeft.addTab("Table Entry", tableEntryScroll);
		tabsBottomLeft.addTab("Form Entry", formEntry);
		tabsBottomLeft.addChangeListener(changeListener);
		panelBottomLeft.setPreferredSize(new Dimension(100,200));
		panelBottomLeft.setLayout(new BorderLayout());
		panelBottomLeft.add(tabsBottomLeft,BorderLayout.CENTER);

		//Bottom Right		
		JPanel panelBottomRight = new JPanel();
		panelBottomRight.setPreferredSize(new Dimension(100,200));
		panelBottomRight.setLayout(new BorderLayout());
		JTabbedPane tabsBottomRight = new JTabbedPane();
		tabsBottomRight.addTab("Field Help", fieldHelp);
		bottomRight = new JScrollPane(tabsBottomRight,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panelBottomRight.add(bottomRight,BorderLayout.CENTER);

		bottom = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,panelBottomLeft,panelBottomRight);
		bottom.setPreferredSize(new Dimension(100,900));

		middleAndBottom = new JSplitPane(JSplitPane.VERTICAL_SPLIT, dragImageFrame, bottom);

		this.getContentPane().add(top,BorderLayout.NORTH);
		this.getContentPane().add(middleAndBottom,BorderLayout.CENTER);
		this.pack();
		this.setMinimumSize(new Dimension(700,700));
		this.setVisible(true);
		batchState.addLogoutListener(this);

		if(xmlFile.exists()){
			middleAndBottom.setDividerLocation(batchState.getVerticalDividePos());
			bottom.setDividerLocation(batchState.getHorizontalDividePos());
			this.setSize(batchState.getWindowWidth(), batchState.getWindowHeight());
			this.setLocation(batchState.getWindowXLoc(), batchState.getWindowYLoc());
		}
		else{
			middleAndBottom.setDividerLocation((0.75));
		}



	}

	ChangeListener changeListener = new ChangeListener(){

		@Override
		public void stateChanged(ChangeEvent e) {
			JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
			int index = sourceTabbedPane.getSelectedIndex();
			if(sourceTabbedPane.getTitleAt(index).toString().equals("Form Entry")){
				formEntry.selectField();
				batchState.setFormEntrySelected(true);
			}
			else{
				batchState.setFormEntrySelected(false);
			}
		}
	};


	@Override
	public void downloadBatch() {
		tabsBottomLeft.remove(tableEntryScroll);
		tableEntry = new TableEntry(batchState);
		tableEntryScroll = new JScrollPane(tableEntry,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		tabsBottomLeft.insertTab("Table Entry",null, tableEntryScroll,null, 0);
		tabsBottomLeft.remove(formEntry);
		formEntry = formEntry.newFormEntry();
		tabsBottomLeft.insertTab("Form Entry", null, formEntry, null, 0);
		dragImageFrame=new DraggingComponent(batchState,batchState.getImageXLoc() , batchState.getImageYLoc());
		middleAndBottom.setTopComponent(dragImageFrame);
		middleAndBottom.revalidate();
		dragImageFrame.revalidate();
		dragImageFrame.repaint();
		for(Component p :buttonMenu.getComponents()){
			((JButton)p).setEnabled(true);
		}


	}

	@Override
	public void performLogout(String function) {

		batchState.setHorizontalDividePos(bottom.getDividerLocation());
		batchState.setVerticalDividePos(middleAndBottom.getDividerLocation());

		batchState.setWindowXLoc(this.getLocationOnScreen().x);
		batchState.setWindowYLoc(this.getLocationOnScreen().y);

		batchState.setZoom(dragImageFrame.getScale());
		batchState.setWindowHeight(this.getHeight());
		batchState.setWindowWidth(this.getWidth());
		batchState.setWindowXLoc(this.getX());
		batchState.setWindowYLoc(this.getY());


		XStream xmlStream = new XStream(new DomDriver());
		String userBatchState = xmlStream.toXML(batchState);
		FileOutputStream fileOutStream = null;
		try {
			fileOutStream = new FileOutputStream(clientFacade.getUser()+".xml");
			fileOutStream.write("<?xml version=\"1.0\"?>".getBytes("UTF-8"));
			byte[] bytes = userBatchState.getBytes("UTF-8");
			fileOutStream.write(bytes);
			batchState.setDataSaved(true);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		finally{
			try {
				fileOutStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		switch(function){
		case "Save":
			break;
		case "Exit":
			this.dispose();
			break;
		case"Logout":	
			clientFacade.logout();
			this.dispose();
			break;
		default:
		}

	}



	@Override
	public void updateImage(String function) {
		if(function.equals("Submit")){
			ArrayList<ArrayList<TableEntryCell>> data = new ArrayList<ArrayList<TableEntryCell>>(batchState.getData());
			ArrayList<String> resultValues = new ArrayList<String>();
			String resultString = new String();
			for(int i=0;i<data.size()-1;i++){
				for(int j=0; j<data.get(i).size();j++){
					if(j!=0){
						resultValues.add(data.get(i).get(j).getValue());
						resultString+=data.get(i).get(j).getValue();
						if(j!=(data.get(i).size()-1)){
							resultString+=",";
						}
					}
				}
				resultString+=";";
			}
			clientFacade.submitBatch(resultString);
		}

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
}