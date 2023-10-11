package io.github.r0land013.showly.web;

import io.javalin.Javalin;


public class ShowlyServer {
    
    private int port;
    private Javalin javalinServer;

    public ShowlyServer(int port) {
        this.port = port;
    }

    public void start() {
        javalinServer = Javalin.create()
        .get("/", ctx -> ctx.result("Welcome to Showly! :)"))
        .start(port);
    }

    public void stop() {
        javalinServer.stop();
    }
}
