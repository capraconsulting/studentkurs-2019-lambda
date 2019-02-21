package no.capraconsulting.kurs2019;

import no.capraconsulting.kurs2019.domain.Event;
import no.capraconsulting.kurs2019.domain.Request;
import no.capraconsulting.kurs2019.domain.Response;
import org.json.simple.JSONObject;

import java.io.IOException;

public class GetOneHandler extends AbstractRequestHandler {
    @Override
    public void handleRequest(Request req, Response res) throws IOException {
        int status;
        JSONObject body = new JSONObject();

        Event event = null;
        String rawId = req.getPathParameter("id");
        if (rawId != null) {
            long id = Long.parseLong(rawId);
            event = eventRepository.getById(id);
            status = 200;
        } else {
            status = 400;
            body.put("message", "Missing id");
        }

        res.send(status, event != null ? event : body.toString());
    }
}
