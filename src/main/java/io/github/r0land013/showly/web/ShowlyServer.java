package io.github.r0land013.showly.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import io.github.r0land013.showly.slides.Slide;
import io.github.r0land013.showly.web.exception.PortBeingUsedException;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.util.JavalinBindException;

import static io.github.r0land013.showly.web.WebSiteGenerator.generateShowlyWebSite;


public class ShowlyServer {
    
    private int port;
    private String presentationName;
    private Javalin javalinServer;
    private List<Slide> slides;
    private List<byte[]> slidesAsBytes;

    

    public ShowlyServer(int port, List<Slide> slides, String presentationName) {
        this.port = port;
        this.slides = slides;
        this.presentationName = presentationName;
    }

    public void start() {
        
        createJavalinWithStaticFileConfig();
        createEndpointToAccessSlideImages();
        prepareShowlyWebSite();
        
        startJavalinServer();
    }

    private void createJavalinWithStaticFileConfig() {
        javalinServer = Javalin.create(config -> {
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";                   // change to host files on a subpath, like '/assets'
                staticFiles.directory = "/static";              // the directory where your files are located
                staticFiles.location = Location.CLASSPATH;
            });
        });
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

    private void prepareShowlyWebSite() {
        String renderedWebSite = generateShowlyWebSite(slides.size(), presentationName);
        
        javalinServer = javalinServer.get("/", ctx -> {
            ctx.contentType("text/html");
            ctx.result(renderedWebSite);
        });
    }

    private void startJavalinServer() {
        try {
            javalinServer.start(port);
        }
        catch (JavalinBindException e) {
            throw new PortBeingUsedException(port);
        }
    }

    public void stop() {
        javalinServer.stop();
    }
}
