package server.handlers;

import java.io.IOException;
import java.net.HttpURLConnection;

import server.ServerFacade;
import shared.communicationClasses.GetFieldsParam;
import shared.communicationClasses.GetFieldsResult;

import com.sun.net.httpserver.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class GetFieldsHandler implements HttpHandler {

	private XStream xmlStream = new XStream(new DomDriver());

	public void handle(HttpExchange exchange) throws IOException{
		GetFieldsResult result= new GetFieldsResult();
		GetFieldsParam param = (GetFieldsParam)xmlStream.fromXML(exchange.getRequestBody());

		try {
			result = ServerFacade.getFields(param);
		} catch (ServerException e) {
			e.printStackTrace();
		}

		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
		xmlStream.toXML(result,exchange.getResponseBody());
		exchange.getResponseBody().close();

	}

}