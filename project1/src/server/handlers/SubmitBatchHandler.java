package server.handlers;

import java.io.IOException;
import java.net.HttpURLConnection;

import server.ServerFacade;
import shared.communicationClasses.SubmitBatchParam;
import shared.communicationClasses.SubmitBatchResult;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class SubmitBatchHandler implements HttpHandler {

	private XStream xmlStream = new XStream(new DomDriver());

	public void handle(HttpExchange exchange) throws IOException{
		SubmitBatchResult result = new SubmitBatchResult();
		SubmitBatchParam param = (SubmitBatchParam)xmlStream.fromXML(exchange.getRequestBody());

		try {
			result = ServerFacade.submitBatch(param);
		} catch (ServerException e) {
			e.printStackTrace();
		}

		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
		xmlStream.toXML(result,exchange.getResponseBody());
		exchange.getResponseBody().close();

	}

}
