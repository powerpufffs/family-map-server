package Handlers;

import Helpers.GsonHelper;
import Responses.FMSResponse;

import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.List;

public abstract class FMSHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");

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
        if (requiresAuth() && getAuthToken(exchange) == null) {
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
            if (res.getError() != null) {
                sendResponseWithMessage(
                    exchange,
                    res.getCode() == 0 ? HttpURLConnection.HTTP_INTERNAL_ERROR : res.getCode(),
                    res.getError().getMessage() == null ? "There was an error in the server." : res.getError().getMessage()
                );
            }
            sendResponse(
                exchange,
                HttpURLConnection.HTTP_OK,
                GsonHelper.serialize(res)
            );
            return;
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

    protected void sendResponseWithMessage(HttpExchange exchange, int code, String message) throws IOException {
        String jsonStr = "{ \"message\":"
                + "\"" + (code != 200 ? "Error. " : "") + String.format("%s", message) + "\"" + "}";
        sendResponse(exchange, code, jsonStr);
    }

    protected void sendResponse(HttpExchange exchange, int code, String jsonStr) throws IOException {
        byte[] bs = jsonStr.getBytes("UTF-8");
        exchange.sendResponseHeaders(code, bs == null ? 0 : bs.length);
        sendJson(bs, exchange.getResponseBody());
        exchange.getResponseBody().close();
    }

    protected void sendJson(byte[] bs, OutputStream os) throws IOException {
        os.write(bs);
        os.flush();
//        OutputStreamWriter osw = new OutputStreamWriter(os);
//        BufferedWriter bw = new BufferedWriter(osw);
//        bw.write(jsonStr);
//        bw.flush();
    }

    String getAuthToken(HttpExchange exchange) {
        List<String> authHeaders = exchange.getRequestHeaders().get("Authorization");
        return (authHeaders == null || authHeaders.isEmpty()) ? null : authHeaders.get(0);
    }

}
