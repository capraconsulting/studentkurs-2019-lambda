package no.capraconsulting.kurs2019.domain;

import java.util.HashMap;
import java.util.Map;

public class ResponseBody {

    private int statusCode;
    private Map<String, String> headers;
    private String body;

    public ResponseBody(int statusCode, String body) {
        this.statusCode = statusCode;
        this.headers = getHeaders();
        this.body = body;
    }

    public ResponseBody(int statusCode) {
        this.statusCode = statusCode;
        this.headers = getHeaders();
    }

    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Headers", "*");
        headers.put("Access-Control-Allow-Methods", "*");
        return headers;
    }
}
