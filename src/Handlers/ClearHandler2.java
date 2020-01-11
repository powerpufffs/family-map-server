package Handlers;

import Responses.ClearResponse;
import Services.ClearService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpURLConnection;

public class ClearHandler2 implements HttpHandler {
    /**
     * @param exchange represents the HttpExchange object passed from the Server
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if(!exchange.getRequestMethod().toUpperCase().equals("POST")) {
                throw new HandlerException("Wrong request method.", HttpURLConnection.HTTP_BAD_REQUEST);
            }
            if(!exchange.getRequestURI().toString().equals("/clear")) {
                throw new HandlerException("Wrong endpoint.", HttpURLConnection.HTTP_NOT_FOUND);
            }

            ClearResponse res = ClearService.clear();

            if(res.getError() != null) {
                throw new HandlerException("Clearing database failed.", HttpURLConnection.HTTP_INTERNAL_ERROR);
            }

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            exchange.getResponseBody().close();

        } catch (HandlerException e) {
            exchange.sendResponseHeaders(e.getResponseCode(), 0);
            exchange.getResponseBody().close();
        }
    }
}