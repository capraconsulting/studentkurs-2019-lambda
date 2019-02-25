package no.capraconsulting.kurs2019.repository;

import no.capraconsulting.kurs2019.domain.Event;
import no.capraconsulting.kurs2019.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class EventRepository {
    private static final String DB_URL = "jdbc:postgresql://" + System.getenv("PG_URL");
    private static final String DB_PORT = System.getenv("PG_PORT");
    private static final String DB_NAME = System.getenv("PG_NAME");
    private static final String DB_USERNAME = System.getenv("PG_USER");
    private static final String DB_PASSWORD = System.getenv("PG_PASSWORD");

    private static final Logger LOGGER = LoggerFactory.getLogger(EventRepository.class);


    private static Event parseResultSet(ResultSet rs) throws SQLException {
        return new Event(rs.getString("id"), rs.getString("data"));
    }

    public List<Event> getAll() {
        LOGGER.info("Executing getAll");
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM events");
            ResultSet rs = preparedStatement.executeQuery();

            List<Event> events = new ArrayList<>();
            while (rs.next()) {
                events.add(parseResultSet(rs));
            }
            LOGGER.info("Executed getAll - Success");
            return events;
        } catch (SQLException e) {
            LOGGER.error("Executed getAll - Failure [message={}]", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private ResultSet performQuery(String query, String... args) throws SQLException {
        Driver driver = new org.postgresql.Driver();
        DriverManager.registerDriver(driver);
        Connection connection = DriverManager.getConnection(DB_URL + ":" + DB_PORT + "/" + DB_NAME, DB_USERNAME, DB_PASSWORD);
        PreparedStatement statement = connection.prepareStatement(query, args);
        return statement.executeQuery();
    }

    private Connection getConnection() {
        try {
            Driver driver = new org.postgresql.Driver();
            DriverManager.registerDriver(driver);
            return DriverManager.getConnection(DB_URL + ":" + DB_PORT + "/" + DB_NAME, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            LOGGER.error("Could not connect to DB", e);
            throw new RuntimeException();
        }
    }

    public Event getById(String id) {
        LOGGER.info("Executing getById [id={}]", id);
        Supplier<RuntimeException> exceptionSupplier = () -> new RuntimeException("Event with id " + id + " not found");
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM events WHERE id = ?");
            preparedStatement.setString(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                LOGGER.info("Executed getById - Success [id={}]", id);
                return parseResultSet(rs);
            } else {
                throw exceptionSupplier.get();
            }
        } catch (SQLException e) {
            LOGGER.error("Executed getById - Failure [id={}, message={}]", id, e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Event create(Event event) {
        LOGGER.info("Executing create");
        try {
            event.setId(UUID.randomUUID().toString());
            LOGGER.debug("Inserting {}", JsonUtils.toJSON(event));
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO events (id, data) VALUES(?, ?::jsonb)");
            preparedStatement.setString(1, event.getId());
            preparedStatement.setString(2, event.getData());
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                LOGGER.info("Executed create - Success");
                return event;
            }
            LOGGER.error("Could not save event to database");
            return null;
        } catch (SQLException e) {
            LOGGER.error("Executed create - Failure [message={}]", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Event update(String id, Event event) {
        LOGGER.info("Executing update [id={}]", id);
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE events SET data=?::jsonb WHERE id=?");
            preparedStatement.setString(1, event.getData());
            preparedStatement.setString(2, id);
            ResultSet rs = preparedStatement.executeQuery();

            LOGGER.info("Executed update - Success");
            if (rs.next()) {
                return event;
            }
            return null;
        } catch (SQLException e) {
            LOGGER.error("Executed update - Failure [id={}, message={}]", id, e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public boolean delete(String id) {
        LOGGER.info("Executing delete [id={}]", id);
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM events WHERE id=?");
            preparedStatement.setString(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            LOGGER.info("Executed delete - Success [id={}]", id);
            return rs.next();
        } catch (SQLException e) {
            LOGGER.error("Executed delete - Failure [id={}, message={}]", id, e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
