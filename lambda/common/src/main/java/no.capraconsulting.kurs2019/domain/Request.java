package no.capraconsulting.kurs2019.domain;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Request {
    private final JSONObject request;

    public Request(InputStream inputStream) throws IOException, ParseException {
        this.request = (JSONObject) new JSONParser().parse(new BufferedReader(new InputStreamReader((inputStream))));
        System.out.println(this.request.toString());
    }

    public String getBody() {
        return (String) request.get("body");
    }

    public String getPathParameter(String key) {
        if (request.get("pathParameters") != null) {
            JSONObject pathParameters = (JSONObject) request.get("pathParameters");
            return (String) pathParameters.get(key);
        }

        return null;
    }
}
