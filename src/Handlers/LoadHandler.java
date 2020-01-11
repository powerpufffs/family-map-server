package Handlers;

import Helpers.GsonHelper;
import Requests.LoadRequest;
import Responses.FMSResponse;
import Services.LoadService;
import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;

public class LoadHandler extends FMSHandler {

    @Override
    public FMSResponse handleRequest(HttpExchange exchange) throws IOException, JsonParseException {
        LoadRequest request = (LoadRequest) GsonHelper.deserialize(exchange.getRequestBody(), LoadRequest.class);
        exchange.getRequestBody().close();
        return LoadService.load(request);
    }

    @Override
    public boolean isValidMethod(String requestMethod) {
        return requestMethod.toLowerCase() == "post";
    }

    @Override
    public boolean isValidEndPoint(String endpoint) {
        return endpoint == "/load";
    }

    @Override
    public boolean requiresAuthToken() {
        return false;
    }

    @Override
    public int getStatusCode(FMSResponse response) {
        String message = response.getMessage();
        if(message.startsWith(FMSResponse.INVALID_REQUEST_DATA_ERROR)) {
            return HttpURLConnection.HTTP_BAD_REQUEST;
        }
        return HttpURLConnection.HTTP_INTERNAL_ERROR;
    }
}
