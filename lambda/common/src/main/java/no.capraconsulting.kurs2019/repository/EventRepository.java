package no.capraconsulting.kurs2019.repository;

import no.capraconsulting.kurs2019.domain.Event;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class EventRepository {
	private static final String DB_URL = System.getenv("PG_URL");
	private static final String DB_USERNAME = System.getenv("PG_USERNAME");
	private static final String DB_PASSWORD = System.getenv("PG_PASSWORD");

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
		try {
			ResultSet rs = performQuery("SELECT * FROM Event");
			List<Event> events = new ArrayList<>();
			while (rs.next()) {
				events.add(parseResultSet(rs));
			}
			return events;
		} catch (SQLException e) {
			e.printStackTrace();
			return events;
		}
	}

	private ResultSet performQuery(String query, String... args) throws SQLException {
		Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
		PreparedStatement statement = connection.prepareStatement(query, args);
		return statement.executeQuery();
	}

	public Event getById(long id) {
		Supplier<RuntimeException> exceptionSupplier = () -> new RuntimeException("Event with id " + id + " not found");
		try {
			ResultSet rs = performQuery("SELECT * FROM Event e WHERE e.id = ?", String.valueOf(id));
			if (rs.next()) {
				return parseResultSet(rs);
			} else {
				throw exceptionSupplier.get();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return events.stream()
					.filter(event -> event.getId() == id)
					.findAny()
					.orElseThrow(exceptionSupplier);
		}
	}

	public boolean create(Event event) {
		try {
			ResultSet rs = performQuery(
					"INSERT INTO Event (Id, Time, Address, Title, Description) VALUES (?, ?, ?, ?, ?)",
					String.valueOf(event.getId()),
					Timestamp.valueOf(event.getTime()).toString(),
					event.getAddress(),
					event.getTitle(),
					event.getDescription()
			);

			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return events.add(event);
		}
	}

	public boolean update(long id, Event event) {
		try {
			ResultSet rs = performQuery(
					"UPDATE Event e SET Time=?, Address=?, Title=?, Description=? WHERE e.Id=?",
					Timestamp.valueOf(event.getTime()).toString(),
					event.getAddress(),
					event.getTitle(),
					event.getDescription(),
					String.valueOf(event.getId())
			);
			return rs.next();
		} catch (SQLException ex) {
			ex.printStackTrace();
			// Hehe
			boolean removed = events.removeIf(e -> e.getId() == id);
			return removed && events.add(event);
		}
	}

	public boolean delete(long id) {
		try {
			ResultSet rs = performQuery("DELETE FROM Event e WHERE e.Id=?", String.valueOf(id));
			return rs.next();
		} catch (SQLException ex) {
			ex.printStackTrace();
			return events.removeIf(e -> e.getId() == id);
		}
	}
}
