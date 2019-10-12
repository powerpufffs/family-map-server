package Models;

public class AuthToken {

    private String tokenId;
    private String personId;
    private String userName;

    public AuthToken(String tokenId, String personId, String userName) {
        this.tokenId = tokenId;
        this.personId = personId;
        this.userName = userName;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTokenId() {
        return tokenId;
    }

    public String getPersonId() {
        return personId;
    }

    public String getUserName() {
        return userName;
    }
}
