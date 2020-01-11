package Handlers;


/**
 * Custom exception that includes a response code. To be used by handlers.
 */
public class HandlerException extends Exception {
    private int responseCode;

    /**
     * @param errorMessage a string describing the error encountered by a handler
     * @param responseCode a number representing an HTTP response code
     */
    public HandlerException(String errorMessage, int responseCode) {
        super(errorMessage);
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}
