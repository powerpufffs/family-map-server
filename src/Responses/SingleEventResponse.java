package Responses;

import Helpers.FMSError;
import Models.Event;

public class SingleEventResponse extends FMSResponse {

    public static final String EVENT_DOESNT_EXIST_ERROR = "Event doesn't exist in the database";
    public static final String INVALID_EVENT_ID = "Invalid eventID parameter";
    public static final String REQUESTED_EVENT_DOESNT_BELONG_TO_USER = "Requested event does not belong to this user";

    private String eventID;
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
    public SingleEventResponse(FMSError error, int code) { super(error, code); }

    /**
     * Creates a SingleEventResponse.
     *
     * @param event the retrieved Event
     */
    public SingleEventResponse(String message, Event event) {
        super(null, null);
        if(event == null) {
            return;
        }

        this.eventID = event.getEventID();
        this.associatedUsername = event.getAssociatedUsername();
        this.personID = event.getPersonId();
        this.latitude = event.getLatitude();
        this.longitude = event.getLongitude();
        this.country = event.getCountry();
        this.city = event.getCity();
        this.eventType = event.getEventType();
        this.year = event.getYear();
    }

    public String getId() {
        return eventID;
    }
    public void setId(String id) {
        this.eventID = id;
    }
    public String getAssociatedUsername() {
        return associatedUsername;
    }
    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }
    public String getPersonId() {
        return personID;
    }
    public void setPersonId(String personID) {
        this.personID = personID;
    }
    public float getLatitude() {
        return latitude;
    }
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
    public float getLongitude() {
        return longitude;
    }
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getType() {
        return eventType;
    }
    public void setType(String type) {
        this.eventType = type;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventID() {
        return eventID;
    }

    public String getPersonID() {
        return personID;
    }

    public String getEventType() {
        return eventType;
    }
}