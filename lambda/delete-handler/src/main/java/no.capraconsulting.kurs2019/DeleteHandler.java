package no.capraconsulting.kurs2019;

import no.capraconsulting.kurs2019.domain.Request;
import no.capraconsulting.kurs2019.domain.Response;
import org.json.simple.JSONObject;

public class DeleteHandler extends AbstractRequestHandler {
    @Override
    public void handleRequest(Request req, Response res) {
        boolean removed;

        String rawId = req.getPathParameter("id");
        if (rawId != null) {
            long id = Long.parseLong(rawId);
            removed = eventRepository.delete(id);
            res.setStatus(removed ? 200 : 400);
        } else {
            res.setStatus(400);
            JSONObject body = new JSONObject();
            body.put("message", "Missing id");
            res.setBody(body.toString());
        }
    }
}
