package no.capraconsulting.kurs2019.domain;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class Response {
    private final JSONObject response;
    private final OutputStream outputStream;

    public Response(JSONObject response, OutputStream outputStream) {
        this.response = response;
        this.outputStream = outputStream;
    }

    public void send(int code, Object body) throws IOException {
        response.put("statusCode", code);
        if (body != null) {
            response.put("body", body.toString());
        }

        OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
        writer.write(response.toString());
        writer.close();
    }
}
