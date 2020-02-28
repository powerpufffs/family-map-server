package Handlers;

import Helpers.FMSError;
import Helpers.GsonHelper;
import Responses.FMSResponse;
import com.google.gson.JsonParseException;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.net.HttpURLConnection;
import java.util.List;

/**
 * A Superclass that defines handler for web API requests.
 */
public abstract class FMSHandler implements HttpHandler {
  private static final String NOT_FOUND_HTML_PATH = "web/HTML/404.html";
  public static final String AUTH_HEADER = "Authorization";

  private static String requestToString(HttpExchange exchange) {
    StringBuilder sb = new StringBuilder();


    sb.append(exchange.getRequestMethod() + " ");
    sb.append(exchange.getRequestURI() + "\n");

    Headers headers = exchange.getRequestHeaders();
    for (String header : headers.keySet()) {
      sb.append("\t" + header + ": ");
      sb.append(headers.get(header).toString() + "\n");
    }
    return sb.toString();
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    if (!isValidEndPoint(exchange.getRequestURI().toString())) {
      sendResponse(
        HttpURLConnection.HTTP_NOT_FOUND,
        new FileInputStream(new File(NOT_FOUND_HTML_PATH)),
        exchange
      );
      return;
    }

    if (!isValidMethod(exchange.getRequestMethod())) {
      sendResponse(HttpURLConnection.HTTP_BAD_REQUEST, exchange);
      return;
    }

    if (!hasRequiredAuthToken(exchange)) {
      FMSResponse response = new FMSResponse(
        new FMSError(FMSResponse.INVALID_AUTH_TOKEN_ERROR)
      );
      sendResponse(
        HttpURLConnection.HTTP_UNAUTHORIZED,
        GsonHelper.serialize(response),
        exchange
      );
      return;
    }

    FMSResponse response = null;

    try {
      response = handleRequest(exchange);
    } catch (JsonParseException e) {
      response =
        new FMSResponse(
          new FMSError(
            FMSResponse.INVALID_REQUEST_DATA_ERROR + ": " + e.getMessage()
          )
        );
    } catch (Exception e) {
      response =
        new FMSResponse(
          new FMSError(
            FMSResponse.INTERNAL_SERVER_ERROR + ": " + e.getMessage()
          )
        );
    }
    sendResponse(response, exchange);
  }

  public abstract FMSResponse handleRequest(HttpExchange exchange)
    throws IOException, JsonParseException;

  public abstract boolean isValidMethod(String requestMethod);

  public abstract boolean isValidEndPoint(String endpoint);

  public abstract boolean requiresAuthToken();

  public abstract int getStatusCode(FMSResponse response);

  private void sendResponse(FMSResponse response, HttpExchange exchange)
    throws IOException {
    if (response == null) { // response already sent
      return;
    }
    String message = response.getMessage();
    sendResponse(
      getStatusCode(response),
      GsonHelper.serialize(response),
      exchange
    );
  }

  public void sendResponse(int status, InputStream body, HttpExchange exchange)
    throws IOException {
    exchange.sendResponseHeaders(status, 0);

    body.transferTo(exchange.getResponseBody());
    body.close();

    exchange.getResponseBody().close();
  }

  public void sendResponse(int status, String body, HttpExchange exchange)
    throws IOException {
    exchange.sendResponseHeaders(status, 0);

    writeToStream(body, exchange.getResponseBody());
    exchange.getResponseBody().close();
  }

  public void sendResponse(int status, HttpExchange exchange)
    throws IOException {
    exchange.sendResponseHeaders(status, 0);
    exchange.getResponseBody().close();
  }

  public void writeToStream(String string, OutputStream stream)
    throws IOException {
    OutputStreamWriter writer = new OutputStreamWriter(stream);
    writer.write(string);
    writer.flush();
  }

  public boolean hasRequiredAuthToken(HttpExchange exchange) {
    return !requiresAuthToken() || getAuthToken(exchange) != null;
  }

  public String getAuthToken(HttpExchange exchange) {
    List<String> authHeaders = exchange.getRequestHeaders().get(AUTH_HEADER);

    if (authHeaders == null || authHeaders.isEmpty()) {
      return null;
    }

    return authHeaders.get(0);
  }
}
