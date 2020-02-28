package Requests;

/**
 * A Class that details the attributes and methods of an FillRequest.
 */
public class FillRequest {

    public static final String INVALID_USERNAME_OR_GENERATIONS_ERROR = "Invalid userName or generations parameter";

    private String userName;
    private int generations;

    /**
     * Constructs a FillRequest
     * @param userName the userName of the user
     * @param generations the number of generation of data to create
     */
    public FillRequest(String userName, int generations) {
        this.userName = userName;
        this.generations = generations;
    }

    /**
     * Creates a message detailing a successful operation
     * @param personCount number of Persons that were added
     * @param eventCount number of Events that were added
     * @return a message containing how many Persons and Events were added to the database
     */
    public static String generateSuccessMessage(int personCount, int eventCount) {
        return "Successfully added " + personCount + " persons and " + " events to the database.";
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public int getGenerations() {
        return generations;
    }

    public void setGenerations(int generations) {
        this.generations = generations;
    }
}