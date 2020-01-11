package Handlers;

import Responses.ClearResponse;
import Services.ClearService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;

public class DefaultHandler implements HttpHandler {
    /**
     * @param exchange represents the HttpExchange object passed from the Server
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if(!exchange.getRequestMethod().toUpperCase().equals("GET")) {
                throw new HandlerException("Wrong request method.", HttpURLConnection.HTTP_BAD_REQUEST);
            }
            if(!exchange.getRequestURI().toString().equals("/")) {
                throw new HandlerException("Wrong endpoint.", HttpURLConnection.HTTP_NOT_FOUND);
            }

            String filePath = "web" + "/index.html";
            File page = new File(filePath);
            OutputStream resBody = exchange.getResponseBody();

            if(page.exists()) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                Files.copy(page.toPath(), resBody);
                exchange.getResponseBody().close();
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                page = new File("web" + "/HTML" + "/404.html");
                Files.copy(page.toPath(), resBody);
                exchange.getResponseBody().close();
            }

        } catch (HandlerException e) {
            exchange.sendResponseHeaders(e.getResponseCode(), 0);
            exchange.getResponseBody().close();
        }
    }
}