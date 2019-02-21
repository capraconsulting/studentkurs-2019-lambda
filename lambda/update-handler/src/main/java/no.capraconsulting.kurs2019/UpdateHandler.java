package no.capraconsulting.kurs2019;

import no.capraconsulting.kurs2019.domain.Event;
import no.capraconsulting.kurs2019.domain.Request;
import no.capraconsulting.kurs2019.domain.Response;
import org.json.simple.JSONObject;

import java.io.IOException;

public class UpdateHandler extends AbstractRequestHandler {
    @Override
    public void handleRequest(Request req, Response res) throws IOException {
        JSONObject body = new JSONObject();

        if (req.getBody() != null) {
            Event event = new Event(req.getBody());

            boolean updated;

            String rawId = req.getPathParameter("id");
            if (rawId != null) {
                long id = Long.parseLong(rawId);
                updated = eventRepository.update(id, event);
                body.put("message", updated ? "Updated Event" : "Could not update event");
            } else {
                body.put("message", "Missing id");
            }
        } else {
            body.put("message", "Missing event");
        }

        res.send(body);
    }
}
