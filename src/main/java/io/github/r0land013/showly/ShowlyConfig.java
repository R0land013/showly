package io.github.r0land013.showly;


public class ShowlyConfig {
    
    final static int DEFAULT_PORT = 80;


    private int port = DEFAULT_PORT;

    private ShowlyConfig(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public static ShowlyConfig create(int port) {
        return new ShowlyConfig(port);
    }

}
