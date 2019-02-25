package no.capraconsulting.kurs2019.domain;

import no.capraconsulting.kurs2019.utils.JsonUtils;

import java.util.UUID;

public class Event {
    private String id;
    private EventData data;


    public Event(String id, String data) {
        this.id = id;
        this.data = JsonUtils.fromJSON(data, EventData.class);
    }

    public Event(String json) {
        this.id = UUID.randomUUID().toString();
        this.data = JsonUtils.fromJSON(json, EventData.class);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData() {
        return JsonUtils.toJSON(data);
    }

    public void setData(String data) {
        this.data = JsonUtils.fromJSON(data, EventData.class);
    }

}
