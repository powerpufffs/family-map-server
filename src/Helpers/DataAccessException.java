package Helpers;

public class DataAccessException extends Exception {
    public DataAccessException(String message)
    {
        super(message);
    }

    DataAccessException()
    {
        super();
    }
}

