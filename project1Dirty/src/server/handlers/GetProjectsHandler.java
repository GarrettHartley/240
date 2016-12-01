package server.handlers;

import java.io.IOException;
import java.net.HttpURLConnection;

import server.ServerFacade;
import shared.communicationClasses.Get_projects_param;
import shared.communicationClasses.Get_projects_result;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class GetProjectsHandler implements HttpHandler {

	private XStream xmlStream = new XStream(new DomDriver());
	
	public void handle(HttpExchange exchange) throws IOException{
		Get_projects_result result= new Get_projects_result();
		
		Get_projects_param param = (Get_projects_param)xmlStream.fromXML(exchange.getRequestBody());
		
		try {
			result = ServerFacade.getProjects(param);
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
		xmlStream.toXML(result,exchange.getResponseBody());
		exchange.getResponseBody().close();
		

		
	}
	
}
