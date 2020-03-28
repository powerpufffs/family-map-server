package Requests;

/**
 * A Class that specifies the attributes and methods of a LoginRequest.
 */
public class LoginRequest {

    private String userName;
    private String password;

    /**
     * Constructs a FillRequest
     * @param userName the userName of the user
     * @param password the password of the user
     */
    public LoginRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }
    public String getPassword() {
        return password;
    }

}
