package server.handlers;

import java.io.IOException;
import java.net.HttpURLConnection;

import server.ServerFacade;
import shared.communicationClasses.SearchParam;
import shared.communicationClasses.SearchResult;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class SearchHandler implements HttpHandler {

	private XStream xmlStream = new XStream(new DomDriver());

	public void handle(HttpExchange exchange) throws IOException{
		SearchResult result= new SearchResult();
		SearchParam param = (SearchParam)xmlStream.fromXML(exchange.getRequestBody());

		try {
			result = ServerFacade.Search(param);
		} catch (ServerException e) {
			e.printStackTrace();
		}

		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
		xmlStream.toXML(result,exchange.getResponseBody());
		exchange.getResponseBody().close();

	}

}
