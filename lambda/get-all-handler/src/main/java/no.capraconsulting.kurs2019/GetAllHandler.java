package no.capraconsulting.kurs2019;

import no.capraconsulting.kurs2019.domain.Event;
import no.capraconsulting.kurs2019.domain.Request;
import no.capraconsulting.kurs2019.domain.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class GetAllHandler extends AbstractRequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetAllHandler.class);

    @Override
    public void handleRequest(Request req, Response res) throws IOException {
        List<Event> events = eventRepository.getAll();
        res.send(events);
    }
}
