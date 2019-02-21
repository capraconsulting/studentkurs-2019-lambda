package no.capraconsulting.kurs2019.repository;

import no.capraconsulting.kurs2019.domain.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class EventRepository {
	private static final String DB_URL = "jdbc:postgresql://" + System.getenv("PG_URL");
	private static final String DB_PORT = System.getenv("PG_PORT");
	private static final String DB_NAME = System.getenv("PG_NAME");
	private static final String DB_USERNAME = System.getenv("PG_USERNAME");
	private static final String DB_PASSWORD = System.getenv("PG_PASSWORD");

	private static final Logger LOGGER = LoggerFactory.getLogger(EventRepository.class);

	private final List<Event> events = Arrays.asList(
			new Event(LocalDateTime.now(), "Arbeidersamfunnets plass 1, 0181 Oslo", "Konsert", "Detta blir gøy"),
			new Event(LocalDateTime.now().plusDays(1), "Stenersgata 2, 0184 Oslo", "Jobb", ":(")
	);

	private static Event parseResultSet(ResultSet rs) throws SQLException {
		return new Event(
				rs.getTimestamp("Time").toLocalDateTime(),
				rs.getString("Address"),
				rs.getString("Title"),
				rs.getString("Description")
		);
	}

	public List<Event> getAll() {
		LOGGER.info("Executing getAll");
		try {
			ResultSet rs = performQuery("SELECT * FROM Event");
			List<Event> events = new ArrayList<>();
			while (rs.next()) {
				events.add(parseResultSet(rs));
			}
			LOGGER.info("Executed getAll - Success");
			return events;
		} catch (SQLException e) {
			LOGGER.error("Executed getAll - Failure [message={}]", e.getMessage());
			e.printStackTrace();
			return events;
		}
	}

	private ResultSet performQuery(String query, String... args) throws SQLException {
		Driver driver = new org.postgresql.Driver();
		DriverManager.registerDriver(driver);
		Connection connection = DriverManager.getConnection(DB_URL + ":" + DB_PORT + "/" + DB_NAME, DB_USERNAME, DB_PASSWORD);
		PreparedStatement statement = connection.prepareStatement(query, args);
		return statement.executeQuery();
	}

	public Event getById(long id) {
		LOGGER.info("Executing getById [id={}]", id);
		Supplier<RuntimeException> exceptionSupplier = () -> new RuntimeException("Event with id " + id + " not found");
		try {
			ResultSet rs = performQuery("SELECT * FROM Event e WHERE e.id = ?", String.valueOf(id));
			if (rs.next()) {
				LOGGER.info("Executed getById - Success [id={}]", id);
				return parseResultSet(rs);
			} else {
				throw exceptionSupplier.get();
			}
		} catch (SQLException e) {
			LOGGER.error("Executed getById - Failure [id={}, message={}]", id, e.getMessage());
			e.printStackTrace();
			return events.stream()
					.filter(event -> event.getId() == id)
					.findAny()
					.orElseThrow(exceptionSupplier);
		}
	}

	public boolean create(Event event) {
		LOGGER.info("Executing create");
		try {
			ResultSet rs = performQuery(
					"INSERT INTO Event (Id, Time, Address, Title, Description) VALUES (?, ?, ?, ?, ?)",
					String.valueOf(event.getId()),
					Timestamp.valueOf(event.getTime()).toString(),
					event.getAddress(),
					event.getTitle(),
					event.getDescription()
			);

			LOGGER.info("Executed create - Success");
			return rs.next();
		} catch (SQLException e) {
			LOGGER.error("Executed create - Failure [message={}]", e.getMessage());
			e.printStackTrace();
			return events.add(event);
		}
	}

	public boolean update(long id, Event event) {
		LOGGER.info("Executing update [id={}]", id);
		try {
			ResultSet rs = performQuery(
					"UPDATE Event e SET Time=?, Address=?, Title=?, Description=? WHERE e.Id=?",
					Timestamp.valueOf(event.getTime()).toString(),
					event.getAddress(),
					event.getTitle(),
					event.getDescription(),
					String.valueOf(event.getId())
			);
			LOGGER.info("Executed update - Success");
			return rs.next();
		} catch (SQLException ex) {
			LOGGER.error("Executed update - Failure [id={}, message={}]", id, ex.getMessage());
			ex.printStackTrace();
			// Hehe
			boolean removed = events.removeIf(e -> e.getId() == id);
			return removed && events.add(event);
		}
	}

	public boolean delete(long id) {
		LOGGER.info("Executing delete [id={}]", id);
		try {
			ResultSet rs = performQuery("DELETE FROM Event e WHERE e.Id=?", String.valueOf(id));
			LOGGER.info("Executed delete - Success [id={}]", id);
			return rs.next();
		} catch (SQLException ex) {
			LOGGER.error("Executed delete - Failure [id={}, message={}]", id, ex.getMessage());
			ex.printStackTrace();
			return events.removeIf(e -> e.getId() == id);
		}
	}
}
