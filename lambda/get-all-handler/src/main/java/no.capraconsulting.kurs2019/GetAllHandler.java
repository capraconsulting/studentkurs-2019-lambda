package no.capraconsulting.kurs2019;

import no.capraconsulting.kurs2019.domain.Event;
import no.capraconsulting.kurs2019.domain.Request;
import no.capraconsulting.kurs2019.domain.Response;

import java.io.IOException;
import java.util.List;

public class GetAllHandler extends AbstractRequestHandler {
    @Override
    public void handleRequest(Request req, Response res) throws IOException {
        List<Event> events = eventRepository.getAll();
        res.send(200, events);
    }
}
