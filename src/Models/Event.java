package Models;

public class Event {

    private String eventId;
    private String associatedUsername;
    private String personID;
    private float latitude;
    private float longitude;
    private String country;
    private String city;
    private String eventType;
    private int year;

    public Event(String eventId, String associatedUsername, String personID, float latitude, float longitude, String country, String city, String eventType, int year) {
        this.eventId = eventId;
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public void setPersonId(String personID) {
        this.personID = personID;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getEventId() {
        return eventId;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public String getPersonId() {
        return personID;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getEventType() {
        return eventType;
    }

    public int getYear() {
        return year;
    }

    public Event clone() {
        return new Event(
            this.eventId,
            this.associatedUsername,
            null,
            this.latitude,
            this.longitude,
            this.country,
            this.city,
            this.eventType,
            this.year
        );
    }
}
