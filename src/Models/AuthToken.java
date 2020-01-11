package Models;

public class AuthToken {

    private String tokenId;
    private String personID;
    private String userName;

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
