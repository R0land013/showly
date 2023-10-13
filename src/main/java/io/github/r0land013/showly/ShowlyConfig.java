package io.github.r0land013.showly;

import java.io.FileInputStream;

public class ShowlyConfig {
    
    final static int DEFAULT_PORT = 80;

    private int port = DEFAULT_PORT;
    private FileInputStream slideFileStream;

    public ShowlyConfig(int port, FileInputStream slideFileStream) {
        this.port = port;
        this.slideFileStream = slideFileStream;
    }

    public int getPort() {
        return port;
    }

    public FileInputStream getSlideFileStream() {
        return slideFileStream;
    }

}
