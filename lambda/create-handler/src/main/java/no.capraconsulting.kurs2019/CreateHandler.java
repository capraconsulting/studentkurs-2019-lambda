package no.capraconsulting.kurs2019;

import no.capraconsulting.kurs2019.domain.Event;
import no.capraconsulting.kurs2019.domain.Request;
import no.capraconsulting.kurs2019.domain.Response;
import no.capraconsulting.kurs2019.domain.ResponseBody;
import no.capraconsulting.kurs2019.utils.JsonUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CreateHandler extends AbstractRequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateHandler.class);

    @Override
    public void handleRequest(Request req, Response res) throws IOException {
        if (req.getBody() != null) {
            Event result = eventRepository.create(new Event(req.getBody()));
            if (result != null) {
                res.send(new ResponseBody(201, JsonUtils.toJSON(result)));
            } else {
                res.send(new ResponseBody(500, "Could not save event to database"));
            }
        } else {
            res.send(new ResponseBody(400, "Could not save event. Body is missing"));
        }
    }
}
