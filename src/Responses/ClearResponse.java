package Responses;

import Helpers.FMSError;

public class ClearResponse extends FMSResponse {

    public static final String CLEAR_SUCCESSFUL_MESSAGE = "Clear succeeded";
    public static final String CLEAR_UNSUCCESSFUL_ERROR = "Clear database unsuccessful.";

    /**
     * @param error error sent when clearing was unsuccessful.
     */
    public ClearResponse(FMSError error) {
        super(null, error);
    }

    /**
     * @param message message sent when clearing was successful.
     */
    public ClearResponse(String message) {
        super(message, null);
    }
}
