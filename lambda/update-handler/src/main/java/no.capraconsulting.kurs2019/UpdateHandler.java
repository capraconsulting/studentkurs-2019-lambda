package no.capraconsulting.kurs2019;

import no.capraconsulting.kurs2019.domain.Event;
import no.capraconsulting.kurs2019.domain.Request;
import no.capraconsulting.kurs2019.domain.Response;
import org.json.simple.JSONObject;

public class UpdateHandler extends AbstractRequestHandler {
    @Override
    public void handleRequest(Request req, Response res) {
        JSONObject body = new JSONObject();

        if (req.getBody() != null) {
            Event event = new Event(req.getBody());

            boolean updated;

            String rawId = req.getPathParameter("id");
            if (rawId != null) {
                long id = Long.parseLong(rawId);
                updated = eventRepository.update(id, event);
                res.setStatus(updated ? 200 : 400);
                body.put("message", updated ? "Updated Event" : "Could not update event");
            } else {
                res.setStatus(400);
                body.put("message", "Missing id");
            }
        } else {
            res.setStatus(400);
            body.put("message", "Missing event");
        }

        res.setBody(body.toString());
    }
}
