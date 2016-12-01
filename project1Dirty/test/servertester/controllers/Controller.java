package servertester.controllers;

import java.util.*;

import client.ClientCommunicator;
import client.ClientException;
import servertester.views.*;
import shared.communicationClasses.Download_batch_param;
import shared.communicationClasses.Download_batch_result;
import shared.communicationClasses.Get_fields_param;
import shared.communicationClasses.Get_fields_result;
import shared.communicationClasses.Get_projects_param;
import shared.communicationClasses.Get_projects_result;
import shared.communicationClasses.Get_sampleImage_param;
import shared.communicationClasses.Get_sampleImage_result;
import shared.communicationClasses.Search_param;
import shared.communicationClasses.Search_result;
import shared.communicationClasses.Submit_batch_param;
import shared.communicationClasses.Submit_batch_result;
import shared.communicationClasses.Validate_user_param;
import shared.communicationClasses.Validate_user_result;

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
		Validate_user_param param = new Validate_user_param();
		Validate_user_result result = new Validate_user_result();
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
		Get_projects_param param = new Get_projects_param();
		Get_projects_result result = new Get_projects_result();
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
		Get_sampleImage_param param = new Get_sampleImage_param();
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
		Download_batch_param param = new Download_batch_param();
		Download_batch_result result = new Download_batch_result();
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
		Get_fields_param param = new Get_fields_param();
		Get_fields_result result = new Get_fields_result();
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
		Submit_batch_param param = new Submit_batch_param();
		Submit_batch_result result = new Submit_batch_result();
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
		Search_param param = new Search_param();
		Search_result result = new Search_result();
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

