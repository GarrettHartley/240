package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import client.ClientCommunicator;
import client.ClientException;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import server.handlers.DownloadBatchHandler;
import server.handlers.DownloadFileHandler;
import server.handlers.GetFieldsHandler;
import server.handlers.GetProjectsHandler;
import server.handlers.GetSampleImageHandler;
import server.handlers.SearchHandler;
import server.handlers.ServerException;
import server.handlers.SubmitBatchHandler;
import server.handlers.ValidateUserHandler;
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

public class Server {
	private static int SERVER_PORT_NUMBER = 8080;
	private static final int MAX_WAITING_CONNECTIONS = 10;
	private HttpServer server;
	private HttpHandler validateUserHandler = new ValidateUserHandler();
	private HttpHandler getFieldsHandler = new GetFieldsHandler();
	private HttpHandler GetProjectsHandler = new GetProjectsHandler();
	private HttpHandler GetSampleImageHandler = new GetSampleImageHandler();
	private HttpHandler DownloadBatchHandler = new DownloadBatchHandler();
	private HttpHandler SubmitBatchHandler = new SubmitBatchHandler();
	private HttpHandler SearchHandler = new SearchHandler();
	private HttpHandler DownloadFile = new DownloadFileHandler();
	
	public static void main(String[] args) {
		if(args.length>0){
		SERVER_PORT_NUMBER=Integer.parseInt(args[0]);
		}

		new Server().run();

	}
	
	public Server(){
		return;
	}
	
	private void run(){
		
		try {
			ServerFacade.initialize();
		} catch (ServerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			server = HttpServer.create(new InetSocketAddress(SERVER_PORT_NUMBER),
												MAX_WAITING_CONNECTIONS);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		server.setExecutor(null);
		
		server.createContext("/validateUser", validateUserHandler);
		server.createContext("/getFields", getFieldsHandler);
		server.createContext("/getProjects", GetProjectsHandler);
		server.createContext("/getSampleImage", GetSampleImageHandler);
		server.createContext("/downloadBatch", DownloadBatchHandler);
		server.createContext("/submitBatch", SubmitBatchHandler);
		server.createContext("/search", SearchHandler);
		server.createContext("/", DownloadFile);
		
		server.start();
	}

}
