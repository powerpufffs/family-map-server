package Requests;

/**
 * A request to the /fill endpoint. Generates "generations" of data for a given user.
 */
public class FillRequest {

    public static final String INVALID_USERNAME_OR_GENERATIONS_ERROR = "Invalid userName or generations parameter";

    private String userName;
    private int generations;

    /**
     * Initializes a FillRequest.
     *
     * @param userName the userName of the user
     * @param generations the number of generation of data to create
     */
    public FillRequest(String userName, int generations) {
        this.userName = userName;
        this.generations = generations;
    }

    /**
     * @return the userName of the user for whom to generate the data
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName of the user for whom to generate the data
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the number of generations of data to generate
     */
    public int getGenerations() {
        return generations;
    }

    /**
     * @param generations the number of generations of data to generate
     */
    public void setGenerations(int generations) {
        this.generations = generations;
    }

    public static String generateSuccessMessage(int personCount, int eventCount) {
        return "Successfully added " + personCount + " persons and " + " events to the database.";
    }

}