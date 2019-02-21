package no.capraconsulting.kurs2019;

import no.capraconsulting.kurs2019.domain.Request;
import no.capraconsulting.kurs2019.domain.Response;
import org.json.simple.JSONObject;

import java.io.IOException;

public class DeleteHandler extends AbstractRequestHandler {
    @Override
    public void handleRequest(Request req, Response res) throws IOException {
        int status;
        JSONObject body = new JSONObject();
        boolean removed;

        String rawId = req.getPathParameter("id");
        if (rawId != null) {
            long id = Long.parseLong(rawId);
            removed = eventRepository.delete(id);
            status = removed ? 200 : 400;
        } else {
            status = 400;
            body.put("message", "Missing id");
        }

        res.send(status, body.toString());
    }
}
