package me.AKZOMBIE74.asynchronous;

import java.io.InputStream;

public class Response {
    private InputStream body;

    public Response(InputStream body) {
        this.body = body;
    }

    public InputStream getBody() {
        return body;
    }
}