package no.capraconsulting.kurs2019.domain;

import org.json.simple.JSONObject;

public class Response {
    private final JSONObject response;

    public Response(JSONObject response) {
        this.response = response;
    }

    public void setStatus(int code) {
        response.put("statusCode", code);
    }

    public void setBody(String body) {
        response.put("body", body);
    }
}
