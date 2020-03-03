package Handlers;

import Helpers.GsonHelper;
import Responses.FMSResponse;
import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.List;

public abstract class FMSHandler2 implements HttpHandler {
    @Override
    public final void handle(HttpExchange exchange) throws IOException {
        if (!isValidRequestMethod(exchange.getRequestMethod())) {
            sendResponseWithMessage(
                exchange,
                HttpURLConnection.HTTP_BAD_REQUEST,
       "Invalid request method."
            );
            return;
        }
        if (!isValidEndpoint(exchange.getRequestURI().toString())) {
            sendResponseWithMessage(
                exchange,
                HttpURLConnection.HTTP_NOT_FOUND,
       "Invalid endpoint."
            );
            return;
        }
        if (requiresAuth() && getAuthToken(exchange) != null) {
            sendResponseWithMessage(
                exchange,
                HttpURLConnection.HTTP_UNAUTHORIZED,
       "No authorization token provided."
            );
            return;
        }

        FMSResponse res = null;
        try {
            res = handleRequest(exchange);
            sendResponse(
                exchange,
                HttpURLConnection.HTTP_OK,
                GsonHelper.serialize(res)
            );
        } catch (IOException e) {
            sendResponseWithMessage(
                exchange,
                HttpURLConnection.HTTP_INTERNAL_ERROR,
                e.getMessage() == null ? "There was an error in the server." : e.getMessage()
            );
            return;
        } catch (JsonParseException e) {
            sendResponseWithMessage(
                exchange,
                HttpURLConnection.HTTP_INTERNAL_ERROR,
                e.getMessage()
            );
            return;
        }
    }

    public abstract FMSResponse handleRequest(HttpExchange exchange) throws IOException;
    public abstract boolean isValidRequestMethod(String method);
    public abstract boolean isValidEndpoint(String endpoint);
    public abstract boolean requiresAuth();

    private void sendResponseWithMessage(HttpExchange exchange, int code, String message) throws IOException {
        String jsonStr = "{ \"message\":" + String.format("\"%s\"", message) + "}";
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        sendResponse(exchange, code, jsonStr);
    }

    private void sendResponse(HttpExchange exchange, int code, String jsonStr) throws IOException {
        exchange.sendResponseHeaders(code, jsonStr == null ? 0 : jsonStr.length());
        sendJson(jsonStr, exchange.getResponseBody());
        exchange.getResponseBody().close();
    }

    private void sendJson(String jsonStr, OutputStream os) throws IOException {
        OutputStreamWriter osw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(osw);
        bw.write(jsonStr);
        bw.flush();
    }

    String getAuthToken(HttpExchange exchange) {
        List<String> authHeaders = exchange.getRequestHeaders().get("Authorization");
        return (authHeaders == null || authHeaders.isEmpty()) ? null : authHeaders.get(0);
    }

}
