package server.handlers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


public class DownloadFileHandler implements HttpHandler {

	public void handle(HttpExchange exchange) throws IOException{
		File file = new File("./database"+File.separator + exchange.getRequestURI().getPath());
		exchange.sendResponseHeaders(200,0);
		Files.copy(file.toPath(), exchange.getResponseBody());
		exchange.getResponseBody().close();

	}

}
