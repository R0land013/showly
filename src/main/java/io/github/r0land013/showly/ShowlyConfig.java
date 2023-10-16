package io.github.r0land013.showly;

public class ShowlyConfig {
    
    final static int DEFAULT_PORT = 80;

    private int port = DEFAULT_PORT;
    private String slideFilePath;
    private String presentationName;

    public ShowlyConfig(int port, String slideFilePath) {
        this.port = port;
        this.slideFilePath = slideFilePath;
    }
    
    public ShowlyConfig(int port, String slideFilePath, String presentationName) {
        this.port = port;
        this.slideFilePath = slideFilePath;
        this.presentationName = presentationName;
    }

    public int getPort() {
        return port;
    }

    public String getSlideFilePath() {
        return slideFilePath;
    }

    public String getPresentationName() {
        return presentationName;
    }

}
