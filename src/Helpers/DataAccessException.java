package Helpers;

public class DataAccessException extends Exception {

    public final static String FIELD_WAS_NULL = "At least one required field was null.";
    public DataAccessException(String message)
    {
        super(message);
    }

    DataAccessException()
    {
        super();
    }
}

