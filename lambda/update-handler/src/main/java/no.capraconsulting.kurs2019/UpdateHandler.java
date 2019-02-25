package no.capraconsulting.kurs2019;

import no.capraconsulting.kurs2019.domain.Event;
import no.capraconsulting.kurs2019.domain.Request;
import no.capraconsulting.kurs2019.domain.Response;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class UpdateHandler extends AbstractRequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateHandler.class);

    @Override
    public void handleRequest(Request req, Response res) throws IOException {
        JSONObject body = new JSONObject();

        if (req.getBody() != null) {
            Event event = new Event(req.getBody());

            boolean updated;

            String id = req.getPathParameter("id");
            if (id != null) {
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
