package server.handlers;

import java.io.IOException;
import java.net.HttpURLConnection;

import server.ServerFacade;
import shared.communicationClasses.ValidateUserParam;
import shared.communicationClasses.ValidateUserResult;

import com.sun.net.httpserver.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class ValidateUserHandler implements HttpHandler {

	private XStream xmlStream = new XStream(new DomDriver());

	public void handle(HttpExchange exchange) throws IOException{

		ValidateUserResult result= new ValidateUserResult();
		ValidateUserParam param = (ValidateUserParam)xmlStream.fromXML(exchange.getRequestBody());

		try {
			result = ServerFacade.validateUser(param);
		} catch (ServerException e) {
			e.printStackTrace();
		}

		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
		xmlStream.toXML(result,exchange.getResponseBody());
		exchange.getResponseBody().close();

	}
}
