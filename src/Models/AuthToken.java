package Models;

/**
 * A Class that defines an Authorization Token for the purposes of authenticating client requests.
 */
public class AuthToken {

    private String tokenId;
    private String personID;
    private String userName;

    /**
     * A constructor that creates AuthToken instances.
     * @param tokenId a unique identifier.
     * @param personID a unique indentifier for the Person to which the token is assigned to.
     * @param userName the username to which the token is being assigned to.
     */
    public AuthToken(String tokenId, String personID, String userName) {
        this.tokenId = tokenId;
        this.personID = personID;
        this.userName = userName;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public void setPersonId(String personID) {
        this.personID = personID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTokenId() {
        return tokenId;
    }

    public String getPersonId() {
        return personID;
    }

    public String getUserName() {
        return userName;
    }
}
