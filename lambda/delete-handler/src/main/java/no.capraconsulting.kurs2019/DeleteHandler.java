package no.capraconsulting.kurs2019;

import no.capraconsulting.kurs2019.domain.Request;
import no.capraconsulting.kurs2019.domain.Response;
import org.json.simple.JSONObject;

import java.io.IOException;

public class DeleteHandler extends AbstractRequestHandler {
    @Override
    public void handleRequest(Request req, Response res) throws IOException {
        JSONObject body = new JSONObject();

        boolean removed;
        String rawId = req.getPathParameter("id");
        if (rawId != null) {
            long id = Long.parseLong(rawId);
            removed = eventRepository.delete(id);
            body.put("message", removed ? "Event deleted" : "Could not delete event");
        } else {
            body.put("message", "Missing id");
        }

        res.send(body);
    }
}
