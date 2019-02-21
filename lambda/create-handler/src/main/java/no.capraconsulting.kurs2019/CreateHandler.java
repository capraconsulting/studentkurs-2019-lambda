package no.capraconsulting.kurs2019;

import no.capraconsulting.kurs2019.domain.Event;
import no.capraconsulting.kurs2019.domain.Request;
import no.capraconsulting.kurs2019.domain.Response;
import org.json.simple.JSONObject;

import java.io.IOException;

public class CreateHandler extends AbstractRequestHandler {
    @Override
    public void handleRequest(Request req, Response res) throws IOException {
        int status;
        JSONObject body = new JSONObject();

        if (req.getBody() != null) {
            boolean result = eventRepository.create(new Event(req.getBody()));
            status = result ? 204 : 500;
            body.put("message", result ? "Event created" : "Internal server error");
        } else {
            status = 400;
            body.put("message", "Missing body");
        }

        res.send(status, body.toString());
    }
}
