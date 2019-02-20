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
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public abstract class AbstractRequestHandler implements RequestStreamHandler {
    final EventRepository eventRepository = new EventRepository();

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        JSONObject response = new JSONObject();
        try {
            handleRequest(new Request(inputStream), new Response(response));
        } catch (ParseException e) {
            response.put("statusCode", 400);
            response.put("exception", e);
        } finally {
            System.out.println(response.toString());
            OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
            writer.write(response.toString());
        }
    }

    public abstract void handleRequest(Request req, Response res);
}
