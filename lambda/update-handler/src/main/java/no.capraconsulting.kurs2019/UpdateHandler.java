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

public class UpdateHandler extends AbstractRequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateHandler.class);

    @Override
    public void handleRequest(Request req, Response res) throws IOException {
        if (req.getBody() != null) {
            Event event = new Event(req.getBody());

            String id = req.getPathParameter("uuid");
            if (id != null) {
                Event updatedEvent = eventRepository.update(id, event);
                if (updatedEvent != null) {
                    res.send(new ResponseBody(200, JsonUtils.toJSON(updatedEvent)));
                } else {
                    res.send(new ResponseBody(500, "Could not update event"));
                }
            } else {
                res.send(new ResponseBody(400, "Missing ID"));
            }
        } else {
            res.send(new ResponseBody(400, "Missing event body"));
        }
    }
}
