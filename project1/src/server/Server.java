package server;

import java.io.IOException;
import java.net.InetSocketAddress;


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


public class Server {
	private static int SERVER_PORT_NUMBER = 39650;
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
	
// Establishes connection over the sever and creates HTTP listeners 
	private void run(){

		try {
			ServerFacade.initialize();
		} catch (ServerException e1) {
			e1.printStackTrace();
		}
		try {
			server = HttpServer.create(new InetSocketAddress(SERVER_PORT_NUMBER),
					MAX_WAITING_CONNECTIONS);
		} catch (IOException e) {
			e.printStackTrace();
		}

		server.setExecutor(null);

		server.createContext("/validateUser", validateUserHandler);
		server.createContext("/getFields", getFieldsHandler);
		server.createContext("/getProjects", GetProjectsHandler);
		server.createContext("/getSampleImage", GetSampleImageHandler);
		server.createContext("/downloadBatch", DownloadBatchHandler);
		server.createContext("/submitBatch", SubmitBatchHandler);
		server.createContext("/SearchFrame", SearchHandler);
		server.createContext("/", DownloadFile);

		server.start();
	}

}
