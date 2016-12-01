package server.handlers;

import java.io.IOException;
import java.net.HttpURLConnection;

import server.ServerFacade;
import shared.communicationClasses.Get_fields_param;
import shared.communicationClasses.Get_fields_result;

import com.sun.net.httpserver.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class GetFieldsHandler implements HttpHandler {
	
	private XStream xmlStream = new XStream(new DomDriver());
	
	public void handle(HttpExchange exchange) throws IOException{
		Get_fields_result result= new Get_fields_result();
		
		Get_fields_param param = (Get_fields_param)xmlStream.fromXML(exchange.getRequestBody());
		
		try {
			result = ServerFacade.getFields(param);
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
		xmlStream.toXML(result,exchange.getResponseBody());
		exchange.getResponseBody().close();

	}

}