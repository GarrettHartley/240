package client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

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

public class ClientCommunicator {

	private static String SERVER_HOST = "localhost";
	private static int SERVER_PORT = 39650;
	private static String URL_PREFIX = "http://" + SERVER_HOST + ":" + SERVER_PORT;
	private static final String HTTP_POST = "POST";

	private XStream xmlStream;

	public ClientCommunicator(String hostName,String serverPort){
		xmlStream = new XStream(new DomDriver());
		SERVER_HOST = hostName;
		SERVER_PORT = Integer.parseInt(serverPort);
		URL_PREFIX = "http://" + SERVER_HOST + ":" + SERVER_PORT;
	}
	
	public GetFieldsResult getFields(GetFieldsParam fieldsParam) throws ClientException{
		Object result = (GetFieldsResult) doPost("/getFields",fieldsParam);
		if(result==null){
			GetFieldsResult failed = new GetFieldsResult();
			failed.setValid(false);
			return failed;
		}
		return (GetFieldsResult) result;
	}

	public GetProjectsResult getProjects(GetProjectsParam projectParam) throws ClientException{
		Object result = (GetProjectsResult) doPost("/getProjects",projectParam);
		if(result==null){
			GetProjectsResult failed = new GetProjectsResult();
			failed.setValid(false);
			return failed;
		}
		return (GetProjectsResult) result;
	}

	public GetSampleBatchResult getSampleImage(GetSampleBatchParam sampleImageParam) throws ClientException{
		sampleImageParam.setUrlPrefix(URL_PREFIX);
		Object result = (GetSampleBatchResult) doPost("/getSampleImage",sampleImageParam);

		if(result==null){
			GetSampleBatchResult failed = new GetSampleBatchResult();
			failed.setValid(false);
			return failed;
		}

		return (GetSampleBatchResult) result;
	}

	public DownloadBatchResult downloadBatch(DownloadBatchParam downloadBatchParam) throws ClientException{
		downloadBatchParam.setUrlPrefix(URL_PREFIX);
		Object result = (DownloadBatchResult) doPost("/downloadBatch",downloadBatchParam);
		if(result==null){
			DownloadBatchResult failed = new DownloadBatchResult();
			failed.setValid(false);
			return failed;
		}
		return (DownloadBatchResult) result;
	}

	public SubmitBatchResult submitBatch(SubmitBatchParam submitBatchParam) throws ClientException{
		Object result = (SubmitBatchResult) doPost("/submitBatch",submitBatchParam);
		if(result==null){
			SubmitBatchResult failed = new SubmitBatchResult();
			failed.setValid(false);
			return failed;
		}
		return (SubmitBatchResult) result;
	}

	public SearchResult search(SearchParam searchParam) throws ClientException{
		searchParam.setUrlPrefix(URL_PREFIX);
		Object result = (SearchResult) doPost("/SearchFrame",searchParam);

		if(result==null){
			SearchResult failed = new SearchResult();
			failed.setValid(false);
			return failed;
		}
		for(SearchResult rslt:((SearchResult) result).getResults()){
		((SearchResult) rslt).setUrlPrefix(URL_PREFIX);
		}
		return (SearchResult) result;
	}

	public ValidateUserResult validateUser(ValidateUserParam userParam) throws ClientException{
		Object result = (ValidateUserResult) doPost("/validateUser",userParam);
		if(result==null){
			ValidateUserResult failed = new ValidateUserResult();
			failed.setValid(false);
			return failed;
		}
		return (ValidateUserResult) result;
	}

// Post HTTP requests to the server
	private Object doPost(String urlPath, Object postData) throws ClientException {
		try {
			URL url = new URL(URL_PREFIX + urlPath);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod(HTTP_POST);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.connect();
			xmlStream.toXML(postData, connection.getOutputStream());
			connection.getOutputStream().close();
			if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
				Object result = xmlStream.fromXML(connection.getInputStream());
			 return result;
			}
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new ClientException(String.format("doPost failed: %s (http code %d)",
						urlPath, connection.getResponseCode()));
			}
		}
		catch (IOException e) {
			return null;
		}
		return null;
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
