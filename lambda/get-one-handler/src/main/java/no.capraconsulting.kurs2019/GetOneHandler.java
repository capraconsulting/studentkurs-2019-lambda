package no.capraconsulting.kurs2019;

import no.capraconsulting.kurs2019.domain.Event;
import no.capraconsulting.kurs2019.domain.Request;
import no.capraconsulting.kurs2019.domain.Response;
import org.json.simple.JSONObject;

public class GetOneHandler extends AbstractRequestHandler {
    @Override
    public void handleRequest(Request req, Response res) {
        Event event = null;
        JSONObject body = new JSONObject();
        String rawId = req.getPathParameter("id");
        if (rawId != null) {
            long id = Long.parseLong(rawId);
            event = eventRepository.getById(id);
        } else {
            res.setStatus(400);
            body.put("message", "Missing id");
        }

        res.setBody(event != null ? event : body.toString());
    }
}
