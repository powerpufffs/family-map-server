package Handlers;

import Helpers.RequestMethod;
import Responses.FMSResponse;
import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;

public class DefaultHandler extends FMSHandler {
    private final static String ROOT_DIR = "web";

    // Override base class because it sends back an html file and not JSON
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!isValidRequestMethod(exchange.getRequestMethod())) {
            super.sendResponseWithMessage(
                exchange,
                HttpURLConnection.HTTP_BAD_REQUEST,
      "Invalid request method."
            );
            return;
        }
//        if (!isValidEndpoint(exchange.getRequestURI().toString())) {
//            super.sendResponseWithMessage(
//                exchange,
//                HttpURLConnection.HTTP_NOT_FOUND,
//      "Invalid endpoint."
//            );
//            return;
//        }

        File page = getFile(exchange.getRequestURI().toString());
        OutputStream resBody = exchange.getResponseBody();

        if(page.exists()) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            Files.copy(page.toPath(), resBody);
            exchange.getResponseBody().flush();
            exchange.getResponseBody().close();
        } else {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
            page = new File("web" + "/HTML" + "/404.html");
            Files.copy(page.toPath(), resBody);
            exchange.getResponseBody().flush();
            exchange.getResponseBody().close();
        }
    }

    @Override
    public FMSResponse handleRequest(HttpExchange exchange) throws IOException {
        return null;
    }

    @Override
    public boolean isValidRequestMethod(String method) {
        return method.equals(RequestMethod.GET.name());
    }

    @Override
    public boolean isValidEndpoint(String endpoint) {
        return getFile(endpoint).exists();
    }

    @Override
    public boolean requiresAuth() {
        return false;
    }

    private File getFile(String path) {
        if (path.equals("/")) {
            path = "/index.html";
        }
        return new File(ROOT_DIR + path);
    }
}