package Models;

public class Event {

    private String eventId;
    private String associatedUsername;
    private String personId;
    private String latitude;
    private String longitude;
    private String country;
    private String city;
    private String eventType;
    private String year;

    public Event(String eventId, String associatedUsername, String personId, String latitude, String longitude, String country, String city, String eventType, String year) {
        this.eventId = eventId;
        this.associatedUsername = associatedUsername;
        this.personId = personId;
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

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
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

    public void setYear(String year) {
        this.year = year;
    }

    public String getEventId() {
        return eventId;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public String getPersonId() {
        return personId;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
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

    public String getYear() {
        return year;
    }
}
