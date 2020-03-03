package Responses;

import Helpers.FMSError;

/**
 * A Class detailing the attributes and methods of a ClearResponse.
 */
public class ClearResponse extends FMSResponse {

    public static final String CLEAR_SUCCESSFUL_MESSAGE = "Clear succeeded.";
    public static final String CLEAR_UNSUCCESSFUL_ERROR = "Clear database unsuccessful.";

    /**
     * Constructs an error ClearResponse
     * @param error error sent when clearing was unsuccessful.
     */
    public ClearResponse(FMSError error) {
        super(null, error);
    }

    /**
     * Constructs a success ClearResponse
     * @param message message sent when clearing was successful.
     */
    public ClearResponse(String message) {
        super(message, null);
    }
}
