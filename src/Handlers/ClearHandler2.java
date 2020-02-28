package Handlers;

import Responses.ClearResponse;
import Services.ClearService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class ClearHandler2 implements HttpHandler {
    /**
     * Handles an http request, ensures that the request is valid and invokes Clear Service.
     *
     * @param exchange represents the HttpExchange object passed from the Server
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (!exchange.getRequestMethod().toUpperCase().equals("POST")) {
                throw new HandlerException("Wrong request method.", HttpURLConnection.HTTP_BAD_REQUEST);
            }
            if (!exchange.getRequestURI().toString().equals("/clear")) {
                throw new HandlerException("Wrong endpoint.", HttpURLConnection.HTTP_NOT_FOUND);
            }

            ClearResponse res = ClearService.clear();
            if (res.getError() != null) {
                throw new HandlerException("Clearing database failed.", HttpURLConnection.HTTP_INTERNAL_ERROR);
            }

            String successMsg = "{ \"message\":" + String.format("\"%s\"", res.getMessage()) + "}";
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, successMsg.length());
            sendJson(successMsg, exchange.getResponseBody());
            exchange.getResponseBody().close();
        } catch (HandlerException e) {
            exchange.sendResponseHeaders(e.getResponseCode(), 0);
            exchange.getResponseBody().close();
        }
    }

    private void sendJson(String jsonStr, OutputStream os) throws IOException {
        OutputStreamWriter osw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(osw);
        bw.write(jsonStr);
        bw.flush();
    }
}