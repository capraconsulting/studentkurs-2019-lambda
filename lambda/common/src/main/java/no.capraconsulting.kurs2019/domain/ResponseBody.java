package no.capraconsulting.kurs2019.domain;

public class ResponseBody {

    private int statusCode;
    private String body;

    public ResponseBody(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public ResponseBody(int statusCode) {
        this.statusCode = statusCode;
    }
}
