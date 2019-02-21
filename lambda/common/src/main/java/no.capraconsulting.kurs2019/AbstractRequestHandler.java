package no.capraconsulting.kurs2019;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import no.capraconsulting.kurs2019.domain.Request;
import no.capraconsulting.kurs2019.domain.Response;
import no.capraconsulting.kurs2019.repository.EventRepository;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class AbstractRequestHandler implements RequestStreamHandler {
    final EventRepository eventRepository = new EventRepository();

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        Response response = new Response(outputStream);
        try {
            handleRequest(new Request(inputStream), response);
        } catch (ParseException e) {
            JSONObject body = new JSONObject();
            body.put("exception", e);
            response.send(body);
        }
    }

    public abstract void handleRequest(Request req, Response res) throws IOException;
}
