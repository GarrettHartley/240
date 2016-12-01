package server.handlers;

import java.io.IOException;
import java.net.HttpURLConnection;

import server.ServerFacade;
import shared.communicationClasses.GetProjectsParam;
import shared.communicationClasses.GetProjectsResult;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class GetProjectsHandler implements HttpHandler {

	private XStream xmlStream = new XStream(new DomDriver());

	public void handle(HttpExchange exchange) throws IOException{
		GetProjectsResult result= new GetProjectsResult();
		GetProjectsParam param = (GetProjectsParam)xmlStream.fromXML(exchange.getRequestBody());

		try {
			result = ServerFacade.getProjects(param);
		} catch (ServerException e) {
			e.printStackTrace();
		}

		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
		xmlStream.toXML(result,exchange.getResponseBody());
		exchange.getResponseBody().close();

	}

}
