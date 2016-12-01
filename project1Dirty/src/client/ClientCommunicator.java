package client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

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

public class ClientCommunicator {

	private static String SERVER_HOST = "localhost";
	private static int SERVER_PORT = 8080;
	private static String URL_PREFIX = "http://" + SERVER_HOST + ":" + SERVER_PORT;
	private static final String HTTP_POST = "POST";

	private XStream xmlStream;

	public ClientCommunicator(String hostName,String serverPort){
		xmlStream = new XStream(new DomDriver());
		SERVER_HOST = hostName;
		SERVER_PORT = Integer.parseInt(serverPort);
		URL_PREFIX = "http://" + SERVER_HOST + ":" + SERVER_PORT;
	}
	


	public Get_fields_result getFields(Get_fields_param fieldsParam) throws ClientException{
		Object result = (Get_fields_result) doPost("/getFields",fieldsParam);
		if(result==null){
			Get_fields_result failed = new Get_fields_result();
			failed.setValid(false);
			return failed;
		}
		return (Get_fields_result) result;
	}

	public Get_projects_result getProjects(Get_projects_param projectParam) throws ClientException{
		Object result = (Get_projects_result) doPost("/getProjects",projectParam);
		if(result==null){
			Get_projects_result failed = new Get_projects_result();
			failed.setValid(false);
			return failed;
		}
		return (Get_projects_result) result;
	}

	public Get_sampleImage_result getSampleImage(Get_sampleImage_param sampleImageParam) throws ClientException{
		sampleImageParam.setUrlPrefix(URL_PREFIX);
		Object result = (Get_sampleImage_result) doPost("/getSampleImage",sampleImageParam);

		if(result==null){
			Get_sampleImage_result failed = new Get_sampleImage_result();
			failed.setValid(false);
			return failed;
		}

		return (Get_sampleImage_result) result;
	}

	public Download_batch_result downloadBatch(Download_batch_param downloadBatchParam) throws ClientException{
		downloadBatchParam.setUrlPrefix(URL_PREFIX);
		Object result = (Download_batch_result) doPost("/downloadBatch",downloadBatchParam);
		if(result==null){
			Download_batch_result failed = new Download_batch_result();
			failed.setValid(false);
			return failed;
		}
		return (Download_batch_result) result;
	}

	public Submit_batch_result submitBatch(Submit_batch_param submitBatchParam) throws ClientException{
		Object result = (Submit_batch_result) doPost("/submitBatch",submitBatchParam);
		if(result==null){
			Submit_batch_result failed = new Submit_batch_result();
			failed.setValid(false);
			return failed;
		}
		return (Submit_batch_result) result;
	}

	public Search_result search(Search_param searchParam) throws ClientException{
		searchParam.setUrlPrefix(URL_PREFIX);
		Object result = (Search_result) doPost("/search",searchParam);

		if(result==null){
			Search_result failed = new Search_result();
			failed.setValid(false);
			return failed;
		}
		return (Search_result) result;
	}

	public Validate_user_result validateUser(Validate_user_param userParam) throws ClientException{
		Object result = (Validate_user_result) doPost("/validateUser",userParam);

		if(result==null){
			Validate_user_result failed = new Validate_user_result();
			failed.setValid(false);
			return failed;
		}
		return (Validate_user_result) result;
	}
		
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
}
