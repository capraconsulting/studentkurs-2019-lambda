package no.capraconsulting.kurs2019;

import no.capraconsulting.kurs2019.domain.Event;
import no.capraconsulting.kurs2019.domain.Request;
import no.capraconsulting.kurs2019.domain.Response;
import org.json.simple.JSONObject;

import java.io.IOException;

public class CreateHandler extends AbstractRequestHandler {
    @Override
    public void handleRequest(Request req, Response res) throws IOException {
        JSONObject body = new JSONObject();

        if (req.getBody() != null) {
            boolean result = eventRepository.create(new Event(req.getBody()));
            body.put("message", result ? "Event created" : "Could not create event");
        } else {
            body.put("message", "Missing request body");
        }

        res.send(body.toString());
    }
}
