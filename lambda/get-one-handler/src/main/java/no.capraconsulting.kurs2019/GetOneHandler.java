package no.capraconsulting.kurs2019;

import no.capraconsulting.kurs2019.domain.Event;
import no.capraconsulting.kurs2019.domain.Request;
import no.capraconsulting.kurs2019.domain.Response;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class GetOneHandler extends AbstractRequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetOneHandler.class);

    @Override
    public void handleRequest(Request req, Response res) throws IOException {
        JSONObject body = new JSONObject();

        Event event = null;
        String rawId = req.getPathParameter("id");
        if (rawId != null) {
            long id = Long.parseLong(rawId);
            event = eventRepository.getById(id);
        } else {
            body.put("message", "Missing id");
        }

        res.send(event != null ? event : body);
    }
}
