package Requests;

import Models.Event;
import Models.Person;
import Models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * A Class that details the attributes and methods of an LoadRequest.
 */
public class LoadRequest {

    private List<User> users;
    private List<Person> persons;
    private List<Event> events;

    /**
     * Constructs a LoadRequest
     * @param users a list of users
     * @param persons a list of persons
     * @param events a list of events
     */
    public LoadRequest(List<User> users, List<Person> persons, List<Event> events) {
        this.users = users == null ? new ArrayList<>() : users;
        this.persons = persons == null ? new ArrayList<>() : persons;
        this.events = events == null ? new ArrayList<>() : events;
    }

    public void replaceNull() {
        this.users = users == null ? new ArrayList<>() : users;
        this.persons = persons == null ? new ArrayList<>() : persons;
        this.events = events == null ? new ArrayList<>() : events;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }
    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<User> getUsers() {
        return users;
    }
    public List<Person> getPersons() {
        return persons;
    }
    public List<Event> getEvents() {
        return events;
    }
}
