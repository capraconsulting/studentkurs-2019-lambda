package no.capraconsulting.kurs2019;

import no.capraconsulting.kurs2019.domain.Request;
import no.capraconsulting.kurs2019.domain.Response;
import no.capraconsulting.kurs2019.domain.ResponseBody;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DeleteHandler extends AbstractRequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteHandler.class);

    @Override
    public void handleRequest(Request req, Response res) throws IOException {
        String id = req.getPathParameter("id");
        if (id != null) {
            boolean removed = eventRepository.delete(id);
            if (removed) {
                res.send(new ResponseBody(200));
            } else {
                res.send(new ResponseBody(500, "Could not delete event from DB"));
            }
        } else {
            res.send(new ResponseBody(400, "Could not delete event, missing ID"));
        }
    }
}
