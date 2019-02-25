package no.capraconsulting.kurs2019.domain;

import com.google.gson.Gson;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class Event {
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
