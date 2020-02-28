package Models;

/**
 * A Class that defines the attributes and methods of an Event.
 */
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

    /**
     * A constructor that creates an Event.
     * @param eventId a unique identifier for an Event.
     * @param associatedUsername the associated username for this Event.
     * @param personID the id of the Person that is associated to this Event.
     * @param latitude the latitude of the coordinate representing the location of the Event.
     * @param longitude the longitude of the coordinate representing the location of the Event.
     * @param country the country in which the Event occurred.
     * @param city the city in which the Event occurred.
     * @param eventType the type of the Event.
     * @param year the year in which the Event occurred.
     */
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

    /**
     * Clones the Event by creating a new Event with the same variables.
     * @return a Event.
     */
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
}
