package no.capraconsulting.kurs2019;

import no.capraconsulting.kurs2019.domain.Event;
import no.capraconsulting.kurs2019.domain.Request;
import no.capraconsulting.kurs2019.domain.Response;
import org.json.simple.JSONObject;

public class CreateHandler extends AbstractRequestHandler {
    @Override
    public void handleRequest(Request req, Response res) {
        JSONObject body = new JSONObject();
        if (req.getBody() != null) {
            boolean result = eventRepository.create(new Event(req.getBody()));
            res.setStatus(result ? 204 : 500);
            body.put("message", result ? "Event created" : "Internal server error");
        } else {
            res.setStatus(400);
            body.put("message", "Missing body");
        }

        res.setBody(body.toString());
    }
}
