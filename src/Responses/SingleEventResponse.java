package Responses;

import Helpers.FMSError;
import Models.Event;

public class SingleEventResponse extends FMSResponse {

    public static final String EVENT_DOESNT_EXIST_ERROR = "Event doesn't exist in the database";

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
     *
     * @param error an FMS error object which contains a message
     *              describing the error
     */
    public SingleEventResponse(FMSError error) {
        super(null, error);
    }

    /**
     * Creates a SingleEventResponse.
     *
     * @param event the retrieved Event
     */
    public SingleEventResponse(String message, Event event) {
        super(message, null);

        if(event == null) {
            return;
        }

        this.eventId = event.getEventId();
        this.associatedUsername = event.getAssociatedUsername();
        this.personID = event.getPersonId();
        this.latitude = event.getLatitude();
        this.longitude = event.getLongitude();
        this.country = event.getCountry();
        this.city = event.getCity();
        this.eventType = event.getEventType();
        this.year = event.getYear();
    }

    /**
     * @return a unique identifier for this event
     */
    public String getId() {
        return eventId;
    }

    /**
     * @param id a unique identifier for this event
     */
    public void setId(String id) {
        this.eventId = id;
    }

    /**
     * @return the userName of the user in whose family map this event is found
     */
    public String getAssociatedUsername() {
        return associatedUsername;
    }

    /**
     * @param associatedUsername the userName of the user in whose family map
     *                           this event is found
     */
    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    /**
     * @return the id of the person in whose life this event occurred
     */
    public String getPersonId() {
        return personID;
    }

    /**
     * @param personID the id of the person in whose life this event occurred
     */
    public void setPersonId(String personID) {
        this.personID = personID;
    }

    /**
     * @return the latitude at which the event occurred
     */
    public float getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude at which the event occurred
     */
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude of the event location
     */
    public float getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude of the event location
     */
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the country in which the event occurred
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country in which the event occurred
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return the city in which the event occurred
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city in which the event occurred
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the type of the event
     */
    public String getType() {
        return eventType;
    }

    /**
     * @param type the event's type
     */
    public void setType(String type) {
        this.eventType = type;
    }

    /**
     * @return the year in which the event occurred
     */
    public int getYear() {
        return year;
    }

    /**
     * @param year the year in which the event occurred
     */
    public void setYear(int year) {
        this.year = year;
    }

}