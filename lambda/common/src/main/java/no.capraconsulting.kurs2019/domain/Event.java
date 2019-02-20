package no.capraconsulting.kurs2019.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

public class Event {
	private final static AtomicLong ID = new AtomicLong(0);

	private long id = ID.incrementAndGet();
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private LocalDateTime time;
	private String address;
	private String title;
	private String description;

	public Event(LocalDateTime time, String address, String title, String description) {
		this.time = time;
		this.address = address;
		this.title = title;
		this.description = description;
	}

	public Event(String json) {
		Gson gson = new Gson();
		Event e = gson.fromJson(json, Event.class);
		this.time = e.time;
		this.address = e.address;
		this.title = e.title;
		this.description = e.description;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
