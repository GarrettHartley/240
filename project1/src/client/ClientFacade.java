package client;

import javax.swing.JOptionPane;

import shared.communicationClasses.DownloadBatchParam;
import shared.communicationClasses.DownloadBatchResult;
import shared.communicationClasses.GetProjectsParam;
import shared.communicationClasses.GetProjectsResult;
import shared.communicationClasses.GetSampleBatchParam;
import shared.communicationClasses.GetSampleBatchResult;
import shared.communicationClasses.SubmitBatchParam;
import shared.communicationClasses.SubmitBatchResult;
import shared.communicationClasses.ValidateUserParam;
import shared.communicationClasses.ValidateUserResult;
import client.gui.indexerWindow.IndexerWindow;
import client.gui.login.LoginFrame;

public class ClientFacade {
	String user;
	String password;
	int pID;
	int batchID;
	public int getpID() {
		return pID;
	}


	public void setpID(int pID) {
		this.pID = pID;
	}

	private static String SERVER_HOST;
	private static int SERVER_PORT;
	ClientCommunicator cc;

	public ClientFacade(String sERVER_HOST, int sERVER_PORT){
		cc = new ClientCommunicator(sERVER_HOST,Integer.toString(sERVER_PORT));	
	}


	public boolean login() {
		ValidateUserParam validateUserParam = new ValidateUserParam();
		ValidateUserResult validateUserResult = new ValidateUserResult();

		validateUserParam.setPassword(password);
		validateUserParam.setUser(user);

		try {
			validateUserResult = cc.validateUser(validateUserParam);
		} catch (ClientException e1) {
			e1.printStackTrace();
		}		

		if( validateUserResult.getValid() == true){
			int response = 	JOptionPane.showOptionDialog(null, "Welcome, "+user+". "
					+ "You have indexed "+validateUserResult.getNumberOfRecords()
					+" records.","Welcome to Indexer",JOptionPane.DEFAULT_OPTION,
					JOptionPane.INFORMATION_MESSAGE, null, null, null);
			if(response == 0){
				IndexerWindow indexerWindow = new IndexerWindow(this);
				return true;
			}
			else if(response == -1){
			}
		}
		else{
			JOptionPane.showOptionDialog(null, "Invalid username of password", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
		}
		return false;
	}
	
	public void logout(){
			
		LoginFrame loginFrame = new LoginFrame(this);
		loginFrame.revalidate();


	}
	
	
		
		public GetProjectsResult getProjects() {
			GetProjectsParam param = new GetProjectsParam();
			GetProjectsResult result = new GetProjectsResult();
			param.setUser(user);
			param.setPassword(password);
			try {
				result = cc.getProjects(param);
			} catch (ClientException e) {
				e.printStackTrace();
			}
			return result;
		}
		
		public String getSampleImage() {
			GetSampleBatchParam param = new GetSampleBatchParam();
			GetSampleBatchResult result = new GetSampleBatchResult();
			param.setUser(user);
			param.setPassword(password);
			param.setProjectID(pID);
			try {
				result = cc.getSampleImage(param);
			} catch (ClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result.getUrlPrefix()+"/"+result.getImgURL();
		}
		
		public DownloadBatchResult downloadBatch() {
			DownloadBatchParam param = new DownloadBatchParam();
			DownloadBatchResult result = new DownloadBatchResult();
			param.setUser(user);
			param.setPassword(password);
			param.setProjectID(pID);
//			System.out.println("this is the pID: "+pID);
			try {
				result = cc.downloadBatch(param);
				
			} catch (ClientException e) {
				e.printStackTrace();
			}
			batchID = result.getBatchID();
			return result;
		}
	//	
	//	private void getFields() {
	//		GetFieldsParam param = new GetFieldsParam();
	//		GetFieldsResult result = new GetFieldsResult();
	//		ClientCommunicator cc = new ClientCommunicator(getView().getHost(),getView().getPort());
	//		param.setUser(getView().getParameterValues()[0]);
	//		param.setPassword(getView().getParameterValues()[1]);
	//		if(getView().getParameterValues()[2].equals("")){
	//			param.setProjectID(-5);
	//		}else{
	//		param.setProjectID(Integer.parseInt(getView().getParameterValues()[2]));
	//		}
	//		try {
	//			result = cc.getFields(param);
	//			getView().setResponse(result.toString());
	//		} catch (ClientException e) {
	//			e.printStackTrace();
	//		}
	//	}
	//	
		public void submitBatch(String values) {
//	//		System.out.println("controler submit: ");
			SubmitBatchParam param = new SubmitBatchParam();
			SubmitBatchResult result = new SubmitBatchResult();
			param.setUser(user);
			param.setPassword(password);
			param.setBatchID(batchID);
			param.setFieldValues(values);
			try {
				result = cc.submitBatch(param);
			} catch (ClientException e) {
				e.printStackTrace();
			}
		}
	//	
	//	private void search() {
	//		SearchParam param = new SearchParam();
	//		SearchResult result = new SearchResult();
	//		ClientCommunicator cc = new ClientCommunicator(getView().getHost(),getView().getPort());
	//		param.setUser(getView().getParameterValues()[0]);
	//		param.setPassword(getView().getParameterValues()[1]);
	//		param.setFieldsToSearch(getView().getParameterValues()[2]);
	//		param.setSearchValues(getView().getParameterValues()[3]);
	//		try {
	//			result = cc.search(param);
	//			getView().setResponse(result.toString());
	//		} catch (ClientException e) {
	//			e.printStackTrace();
	//		}
	//	}

	public int getBatchID() {
			return batchID;
		}


		public void setBatchID(int batchID) {
			this.batchID = batchID;
		}


	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static String getSERVER_HOST() {
		return SERVER_HOST;
	}

	public static void setSERVER_HOST(String sERVER_HOST) {
		SERVER_HOST = sERVER_HOST;
	}

	public static int getSERVER_PORT() {
		return SERVER_PORT;
	}

	public static void setSERVER_PORT(int sERVER_PORT) {
		SERVER_PORT = sERVER_PORT;
	}


}