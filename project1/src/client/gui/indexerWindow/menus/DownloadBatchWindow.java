package client.gui.indexerWindow.menus;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import client.ClientFacade;
import client.gui.BatchState;
import shared.communicationClasses.DownloadBatchResult;
import shared.communicationClasses.GetProjectsResult;

public class DownloadBatchWindow extends JDialog{

	JButton viewSample;
	JButton cancel;
	JButton download;
	JComboBox projects;
	ClientFacade clientFacade;
	GetProjectsResult currentProjects;
	BatchState batchState;
	JDialog self = this;

	public DownloadBatchWindow(BatchState batchState, ClientFacade clientFacade){

		this.clientFacade = clientFacade;
		this.batchState = batchState;
		viewSample = new JButton("View Sample");
		viewSample.addActionListener(viewSampleListener);
		cancel = new JButton("Cancel");
		cancel.addActionListener(cancelListener);
		download = new JButton("Download");
		download.addActionListener(downloadBatchListener);
		if(batchState.isHasBatch()){
			System.out.println("disabled it");
			download.setEnabled(false);
		}
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS));
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		projects = new JComboBox();

		currentProjects = new GetProjectsResult();
		currentProjects = clientFacade.getProjects();

		for(GetProjectsResult pstr: currentProjects.getResults()){
			String listItem = new String(pstr.getProjectTitle());
			projects.addItem(listItem);
		}

		projects.setPreferredSize(new Dimension(100,20));
		projects.setMaximumSize(new Dimension(200, 20));
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
		top.add(projects);

		JPanel bottom = new JPanel();
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));
		bottom.add(cancel);
		bottom.add(download);
		bottom.add(viewSample);

		this.getContentPane().add(top);
		this.getContentPane().add(bottom);
		this.pack();
		this.setSize(new Dimension(400, 200));
		this.setMaximumSize(new Dimension(400, 200));
		this.setMinimumSize(new Dimension(400, 200));
		this.setResizable(false);


		this.setVisible(true);
	}

	private ActionListener viewSampleListener = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			for(GetProjectsResult pstr: currentProjects.getResults()){
				if(pstr.getProjectTitle().equals(projects.getSelectedItem())){
					clientFacade.setpID(pstr.getProjectID());

					try {
						BufferedImage bi = ImageIO.read(new URL(clientFacade.getSampleImage()));
						JOptionPane.showMessageDialog(null, null, clientFacade.getSampleImage(), JOptionPane.CANCEL_OPTION, new ImageIcon(bi));
						new ImageIcon(bi);
					} catch (MalformedURLException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}				}		
			}
		}
	};

	private ActionListener cancelListener = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			self.dispose();
		}

	};

	private ActionListener downloadBatchListener = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			for(GetProjectsResult pstr: currentProjects.getResults()){
				if(pstr.getProjectTitle().equals(projects.getSelectedItem())){
					clientFacade.setpID(pstr.getProjectID());
					DownloadBatchResult downloadBatchResult = new DownloadBatchResult();
					downloadBatchResult = clientFacade.downloadBatch();
					batchState.setHasBatch(true);
					batchState.setBatchID(downloadBatchResult.getBatchID());
					batchState.setFields(downloadBatchResult.getFields());
					batchState.setCellWidth(downloadBatchResult.getFields().get(0).getWidth());
					batchState.setRecordHeight(downloadBatchResult.getRecordHeight());
					batchState.setFirstYCoord(downloadBatchResult.getFirstYCoord());
					batchState.setFullImgURL(downloadBatchResult.getFullImgURL());
					batchState.setNumberOfFields(downloadBatchResult.getNumberOfFields());
					batchState.setNumberOfRecords(downloadBatchResult.getNumberOfRecords());
					batchState.setProjectID(downloadBatchResult.getProjectID());
					batchState.setUrlPrefix(downloadBatchResult.getUrlPrefix()+"/");
					batchState.setData();
					batchState.downloadBatch();
					self.dispose();
				}
			}
		}
	};

	public JButton getDownload() {
		return download;
	}

	public void setDownload(JButton download) {
		this.download = download;
	}
}