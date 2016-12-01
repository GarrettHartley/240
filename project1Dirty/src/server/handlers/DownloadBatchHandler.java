package server.handlers;

import java.io.IOException;
import java.net.HttpURLConnection;

import server.ServerFacade;
import shared.communicationClasses.Download_batch_param;
import shared.communicationClasses.Download_batch_result;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class DownloadBatchHandler implements HttpHandler {

	private XStream xmlStream = new XStream(new DomDriver());
	
	public void handle(HttpExchange exchange) throws IOException{
		Download_batch_result result= new Download_batch_result();
		
		Download_batch_param param = (Download_batch_param)xmlStream.fromXML(exchange.getRequestBody());
		
		try {
			result = ServerFacade.downloadBatch(param);
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
		xmlStream.toXML(result,exchange.getResponseBody());
		exchange.getResponseBody().close();
	
	}
	
}
