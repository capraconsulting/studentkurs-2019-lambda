package no.capraconsulting.kurs2019;

import com.google.gson.Gson;
import no.capraconsulting.kurs2019.domain.Event;
import no.capraconsulting.kurs2019.domain.Request;
import no.capraconsulting.kurs2019.domain.Response;

import java.util.List;

public class GetAllHandler extends AbstractRequestHandler {
    @Override
    public void handleRequest(Request req, Response res) {
        List<Event> events = eventRepository.getAll();
        res.setStatus(200);
        res.setBody(new Gson().toJson(events));
    }
}
