package server.handlers;

import java.io.IOException;
import java.net.HttpURLConnection;

import server.ServerFacade;
import shared.communicationClasses.Submit_batch_param;
import shared.communicationClasses.Submit_batch_result;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class SubmitBatchHandler implements HttpHandler {

	private XStream xmlStream = new XStream(new DomDriver());

	public void handle(HttpExchange exchange) throws IOException{
//		System.out.println("submit handler:");
		Submit_batch_result result = new Submit_batch_result();
		Submit_batch_param param = (Submit_batch_param)xmlStream.fromXML(exchange.getRequestBody());

		try {
			result = ServerFacade.submitBatch(param);
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
		xmlStream.toXML(result,exchange.getResponseBody());
		exchange.getResponseBody().close();

	}

}
