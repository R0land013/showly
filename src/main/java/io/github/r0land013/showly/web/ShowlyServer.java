package io.github.r0land013.showly.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import io.github.r0land013.showly.slides.Slide;
import io.javalin.Javalin;


public class ShowlyServer {
    
    private int port;
    private Javalin javalinServer;
    private List<Slide> slides;
    private List<byte[]> slidesAsBytes;

    public ShowlyServer(int port, List<Slide> slides) {
        this.port = port;
        this.slides = slides;
    }

    public void start() {
        javalinServer = Javalin.create()
        .get("/", ctx -> ctx.result("Welcome to Showly! :)"));
        
        createEndpointToAccessSlideImages();

        javalinServer.start(port);
    }
    
    private void createEndpointToAccessSlideImages () {
        
        prepareSlidesToShareThroughNetwork();
        
        for(Slide aSlide: slides) {
            
            String url = String.format("/slides/%d", aSlide.getSlideIndex()); 
            
            javalinServer.get(url, ctx -> {
                ctx.contentType("image/png");
                ctx.result(slidesAsBytes.get(aSlide.getSlideIndex()));
            });
        }
    }
    
    private void prepareSlidesToShareThroughNetwork() {
        slidesAsBytes = new LinkedList<byte[]>();
        try {
            
            for(Slide aSlide: slides) {

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(aSlide.getImageStream(), "png", baos);
                byte[] imageData = baos.toByteArray();
                slidesAsBytes.add(imageData);
            }
        
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void stop() {
        javalinServer.stop();
    }
}
