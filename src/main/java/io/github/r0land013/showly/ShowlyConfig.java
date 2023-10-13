package io.github.r0land013.showly;

public class ShowlyConfig {
    
    final static int DEFAULT_PORT = 80;

    private int port = DEFAULT_PORT;
    private String slideFilePath;

    public ShowlyConfig(int port, String slideFilePath) {
        this.port = port;
        this.slideFilePath = slideFilePath;
    }

    public int getPort() {
        return port;
    }

    public String getSlideFilePath() {
        return slideFilePath;
    }

}
