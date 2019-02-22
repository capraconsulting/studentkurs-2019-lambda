package no.capraconsulting.kurs2019;

import no.capraconsulting.kurs2019.domain.Request;
import no.capraconsulting.kurs2019.domain.Response;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DeleteHandler extends AbstractRequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteHandler.class);

    @Override
    public void handleRequest(Request req, Response res) throws IOException {
        JSONObject body = new JSONObject();

        boolean removed;
        String id = req.getPathParameter("id");
        if (id != null) {
            removed = eventRepository.delete(id);
            body.put("message", removed ? "Event deleted" : "Could not delete event");
        } else {
            body.put("message", "Missing id");
        }

        res.send(body);
    }
}
