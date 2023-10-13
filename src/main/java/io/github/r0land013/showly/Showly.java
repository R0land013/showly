package io.github.r0land013.showly;

import java.io.IOException;
import java.util.List;
import io.github.r0land013.showly.slides.Slide;
import static io.github.r0land013.showly.slides.SlideExtractor.extractSlidesFromFile;;
import io.github.r0land013.showly.slides.exception.InvalidSlideFile;
import io.github.r0land013.showly.web.ShowlyServer;

public class Showly {

    private ShowlyConfig showlyConfig;
    private ShowlyServer showlyServer;

    public Showly(ShowlyConfig config) {
        showlyConfig = config;
    }

    public List<Slide> show() throws IOException, InvalidSlideFile {
        List<Slide> slides = extractSlidesFromFile(showlyConfig.getSlideFilePath());

        startWebServer();
        return slides.subList(0, slides.size());
    }

    private void startWebServer() {
        showlyServer = new ShowlyServer(showlyConfig.getPort());
        showlyServer.start();
    }

    public void stop() {
        showlyServer.stop();
    }
}
