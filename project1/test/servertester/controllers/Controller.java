package servertester.controllers;

import java.util.*;

import client.ClientCommunicator;
import client.ClientException;
import servertester.views.*;
import shared.communicationClasses.DownloadBatchParam;
import shared.communicationClasses.DownloadBatchResult;
import shared.communicationClasses.GetFieldsParam;
import shared.communicationClasses.GetFieldsResult;
import shared.communicationClasses.GetProjectsParam;
import shared.communicationClasses.GetProjectsResult;
import shared.communicationClasses.GetSampleBatchParam;
import shared.communicationClasses.GetSampleBatchResult;
import shared.communicationClasses.SearchParam;
import shared.communicationClasses.SearchResult;
import shared.communicationClasses.SubmitBatchParam;
import shared.communicationClasses.SubmitBatchResult;
import shared.communicationClasses.ValidateUserParam;
import shared.communicationClasses.ValidateUserResult;

public class Controller implements IController {

	private IView _view;
	
	public Controller() {
		return;
	}
	
	public IView getView() {
		return _view;
	}
	
	public void setView(IView value) {
		_view = value;
	}
	
	// IController methods
	//
	
	@Override
	public void initialize() {
		getView().setHost("localhost");
		getView().setPort("39640");
		operationSelected();
	}

	@Override
	public void operationSelected() {
		ArrayList<String> paramNames = new ArrayList<String>();
		paramNames.add("User");
		paramNames.add("Password");
		
		switch (getView().getOperation()) {
		case VALIDATE_USER:
			break;
		case GET_PROJECTS:
			break;
		case GET_SAMPLE_IMAGE:
			paramNames.add("Project");
			break;
		case DOWNLOAD_BATCH:
			paramNames.add("Project");
			break;
		case GET_FIELDS:
			paramNames.add("Project");
			break;
		case SUBMIT_BATCH:
			paramNames.add("Batch");
			paramNames.add("Record Values");
			break;
		case SEARCH:
			paramNames.add("Fields");
			paramNames.add("Search Values");
			break;
		default:
			assert false;
			break;
		}
		
		getView().setRequest("");
		getView().setResponse("");
		getView().setParameterNames(paramNames.toArray(new String[paramNames.size()]));
	}

	@Override
	public void executeOperation() {
		switch (getView().getOperation()) {
		case VALIDATE_USER:
			validateUser();
			break;
		case GET_PROJECTS:
			getProjects();
			break;
		case GET_SAMPLE_IMAGE:
			getSampleImage();
			break;
		case DOWNLOAD_BATCH:
			downloadBatch();
			break;
		case GET_FIELDS:
			getFields();
			break;
		case SUBMIT_BATCH:
			submitBatch();
			break;
		case SEARCH:
			search();
			break;
		default:
			assert false;
			break;
		}
	}
	
	private void validateUser() {
		ValidateUserParam param = new ValidateUserParam();
		ValidateUserResult result = new ValidateUserResult();
		ClientCommunicator cc = new ClientCommunicator(getView().getHost(),getView().getPort());
		param.setUser(getView().getParameterValues()[0]);
		param.setPassword(getView().getParameterValues()[1]);
		try {
			result = cc.validateUser(param);
			getView().setResponse(result.toString());
		} catch (ClientException e) {
			e.printStackTrace();
		}
	}
	
	private void getProjects() {
		GetProjectsParam param = new GetProjectsParam();
		GetProjectsResult result = new GetProjectsResult();
		ClientCommunicator cc = new ClientCommunicator(getView().getHost(),getView().getPort());
		param.setUser(getView().getParameterValues()[0]);
		param.setPassword(getView().getParameterValues()[1]);
		try {
			result = cc.getProjects(param);
			getView().setResponse(result.toString());
		} catch (ClientException e) {
			e.printStackTrace();
		}
		
	}
	
	private void getSampleImage() {
		GetSampleBatchParam param = new GetSampleBatchParam();
		ClientCommunicator cc = new ClientCommunicator(getView().getHost(),getView().getPort());
		param.setUser(getView().getParameterValues()[0]);
		param.setPassword(getView().getParameterValues()[1]);
		param.setProjectID(Integer.parseInt(getView().getParameterValues()[2]));
		try {
			getView().setResponse(cc.getSampleImage(param).toString());
		} catch (ClientException e) {
			e.printStackTrace();
		}
	}
	
	private void downloadBatch() {
		DownloadBatchParam param = new DownloadBatchParam();
		DownloadBatchResult result = new DownloadBatchResult();
		ClientCommunicator cc = new ClientCommunicator(getView().getHost(),getView().getPort());
		param.setUser(getView().getParameterValues()[0]);
		param.setPassword(getView().getParameterValues()[1]);
		param.setProjectID(Integer.parseInt(getView().getParameterValues()[2]));
		try {
			result = cc.downloadBatch(param);
			getView().setResponse(result.toString());
		} catch (ClientException e) {
			e.printStackTrace();
		}
	}
	
	private void getFields() {
		GetFieldsParam param = new GetFieldsParam();
		GetFieldsResult result = new GetFieldsResult();
		ClientCommunicator cc = new ClientCommunicator(getView().getHost(),getView().getPort());
		param.setUser(getView().getParameterValues()[0]);
		param.setPassword(getView().getParameterValues()[1]);
		if(getView().getParameterValues()[2].equals("")){
			param.setProjectID(-5);
		}else{
		param.setProjectID(Integer.parseInt(getView().getParameterValues()[2]));
		}
		try {
			result = cc.getFields(param);
			getView().setResponse(result.toString());
		} catch (ClientException e) {
			e.printStackTrace();
		}
	}
	
	private void submitBatch() {
//		System.out.println("controler submit: ");
		SubmitBatchParam param = new SubmitBatchParam();
		SubmitBatchResult result = new SubmitBatchResult();
		ClientCommunicator cc = new ClientCommunicator(getView().getHost(),getView().getPort());
		param.setUser(getView().getParameterValues()[0]);
		param.setPassword(getView().getParameterValues()[1]);
		param.setBatchID(Integer.parseInt(getView().getParameterValues()[2]));
		param.setFieldValues(getView().getParameterValues()[3]);
		try {
			result = cc.submitBatch(param);
			getView().setResponse(result.toString());
		} catch (ClientException e) {
			e.printStackTrace();
		}
	}
	
	private void search() {
		SearchParam param = new SearchParam();
		SearchResult result = new SearchResult();
		ClientCommunicator cc = new ClientCommunicator(getView().getHost(),getView().getPort());
		param.setUser(getView().getParameterValues()[0]);
		param.setPassword(getView().getParameterValues()[1]);
		param.setFieldsToSearch(getView().getParameterValues()[2]);
		param.setSearchValues(getView().getParameterValues()[3]);
		try {
			result = cc.search(param);
			getView().setResponse(result.toString());
		} catch (ClientException e) {
			e.printStackTrace();
		}
	}

}

