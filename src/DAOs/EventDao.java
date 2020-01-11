package DAOs;

import Helpers.DataAccessException;
import Models.AuthToken;
import Models.Event;

import java.sql.*;
import java.util.List;

public class EventDao {

    private final Connection connection;

    public EventDao(Connection connection) {
        this.connection = connection;
    }

    public void insert(Event event) throws DataAccessException {
        String sql = "INSERT INTO event (event_id, associated_userName, person_id, latitude, "
                + "longitude, country, city, event_type, year) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement statement = null;
        int i = 1;

        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, event.getEventId());
            stmt.setString(2, event.getAssociatedUsername());
            stmt.setString(3, event.getPersonId());
            stmt.setFloat(4, event.getLatitude());
            stmt.setFloat(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());
            stmt.executeUpdate();
        } catch(SQLException e) {
            throw new DataAccessException("Error encountered while inserting new event into the database");
        }
    }

    public Event readOneEvent(String eventId) throws DataAccessException {
        if(eventId == null)
            throw new DataAccessException("eventId was null when attempting to read one event");

        Event event = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM event WHERE event_id = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, eventId);
            rs = stmt.executeQuery();

            if(rs.next()) {
                event = new Event(
                    rs.getString("event_id"),
                    rs.getString("associated_userName"),
                    rs.getString("person_id"),
                    rs.getFloat("latitude"),
                    rs.getFloat("longitude"),
                    rs.getString("country"),
                    rs.getString("city"),
                    rs.getString("event_type"),
                    Integer.parseInt(rs.getString("year"))
                );
                return event;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while reading one event from the database");
        }
        return null;
    }

    public Event[] readAllEvents(AuthToken authToken) throws Error, DataAccessException {
        if(authToken == null)
            throw new DataAccessException("Error. authToken was null when attempting to read one authToken");

        List<Event> eventList = null;

        try(
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM person")
        ) {
            while(rs.next()) {
                eventList.add(new Event(
                    rs.getString("event_id"),
                    rs.getString("associated_userName"),
                    rs.getString("person_id"),
                    rs.getFloat("latitude"),
                    rs.getFloat("longitude"),
                    rs.getString("country"),
                    rs.getString("city"),
                    rs.getString("event_type"),
                    rs.getInt("year")
                ));
            }

            return eventList.toArray(new Event[eventList.size()]);
        } catch(SQLException e) {
            throw new DataAccessException("Error countered while reading all events");
        }
    }

    public void deleteAllEvents() throws DataAccessException {
        try(Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM event");
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while deleting all events");
        }
    }

    public void deleteAllEventsForUser(String userName) throws DataAccessException {
        String sql = "DELETE FROM event WHERE associated_userName = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userName);
            stmt.executeUpdate();
        } catch(SQLException e) {
            throw new DataAccessException("Error encountered while deleting all events for given user.");
        }
    }



}
