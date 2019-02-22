package no.capraconsulting.kurs2019.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

public class Event {
	private final static AtomicLong ID = new AtomicLong(0);

	private String id;
	private String data;


	public Event() {
		id = "";
		data = "";
	}

	public Event(String id, String data) {
		this.id = id;
		this.data = data;
	}

	public Event(String json) {
		Gson gson = new Gson();
		Event e = gson.fromJson(json, Event.class);
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
