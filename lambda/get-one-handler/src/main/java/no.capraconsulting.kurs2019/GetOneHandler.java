package no.capraconsulting.kurs2019;

import no.capraconsulting.kurs2019.domain.Event;
import no.capraconsulting.kurs2019.domain.Request;
import no.capraconsulting.kurs2019.domain.Response;
import no.capraconsulting.kurs2019.domain.ResponseBody;
import no.capraconsulting.kurs2019.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class GetOneHandler extends AbstractRequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetOneHandler.class);

    @Override
    public void handleRequest(Request req, Response res) throws IOException {
        String id = req.getPathParameter("id");
        if (id != null) {
            Event event = eventRepository.getById(id);
            if (event != null) {
                res.send(new ResponseBody(200, JsonUtils.toJSON(event)));
            } else {
                res.send(new ResponseBody(404, "Could not find event"));
            }
        } else {
            res.send(new ResponseBody(400, "Missing ID"));
        }
    }
}
