package server.handlers;

import java.io.IOException;
import java.net.HttpURLConnection;

import server.ServerFacade;
import shared.communicationClasses.Search_param;
import shared.communicationClasses.Search_result;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class SearchHandler implements HttpHandler {

	private XStream xmlStream = new XStream(new DomDriver());
	
	public void handle(HttpExchange exchange) throws IOException{
		Search_result result= new Search_result();
		
		Search_param param = (Search_param)xmlStream.fromXML(exchange.getRequestBody());
		
		try {
			result = ServerFacade.Search(param);
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
		xmlStream.toXML(result,exchange.getResponseBody());
		exchange.getResponseBody().close();

	}
	
}
