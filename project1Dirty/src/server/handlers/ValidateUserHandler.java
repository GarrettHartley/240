package server.handlers;

import java.io.IOException;
import java.net.HttpURLConnection;

import server.ServerFacade;
import shared.communicationClasses.Validate_user_param;
import shared.communicationClasses.Validate_user_result;

import com.sun.net.httpserver.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class ValidateUserHandler implements HttpHandler {
	
	private XStream xmlStream = new XStream(new DomDriver());
	
	public void handle(HttpExchange exchange) throws IOException{
		
		Validate_user_result result= new Validate_user_result();
		Validate_user_param param = (Validate_user_param)xmlStream.fromXML(exchange.getRequestBody());
		
		try {
			result = ServerFacade.validateUser(param);
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
		xmlStream.toXML(result,exchange.getResponseBody());
		exchange.getResponseBody().close();

	}
}
