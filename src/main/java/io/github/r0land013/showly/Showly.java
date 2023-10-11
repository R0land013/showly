package io.github.r0land013.showly;
import io.github.r0land013.showly.web.ShowlyServer;

public class Showly {
    
    private ShowlyConfig showlyConfig;
    private ShowlyServer showlyServer;

    public Showly(ShowlyConfig config) {
        showlyConfig = config;
    }

    public void show() {
        showlyServer = new ShowlyServer(showlyConfig.getPort());
        showlyServer.start();
    }

    public void stop() {
        showlyServer.stop();
    }
}
