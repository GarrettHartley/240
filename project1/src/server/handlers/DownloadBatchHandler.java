package server.handlers;

import java.io.IOException;
import java.net.HttpURLConnection;

import server.ServerFacade;
import shared.communicationClasses.DownloadBatchParam;
import shared.communicationClasses.DownloadBatchResult;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class DownloadBatchHandler implements HttpHandler {

	private XStream xmlStream = new XStream(new DomDriver());

	public void handle(HttpExchange exchange) throws IOException{
		DownloadBatchResult result= new DownloadBatchResult();

		DownloadBatchParam param = (DownloadBatchParam)xmlStream.fromXML(exchange.getRequestBody());

		try {
			result = ServerFacade.downloadBatch(param);
		} catch (ServerException e) {
			e.printStackTrace();
		}
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
		xmlStream.toXML(result,exchange.getResponseBody());
		exchange.getResponseBody().close();

	}

}