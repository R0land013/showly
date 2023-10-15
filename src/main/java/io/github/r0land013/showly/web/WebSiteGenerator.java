package io.github.r0land013.showly.web;

import java.io.IOException;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;

public class WebSiteGenerator {
    
    private static class PresentationData {
        
        private int slideQuantity;

        public PresentationData(int slideQuantity) {
            this.slideQuantity = slideQuantity;
        }

        public int getSlideQuantity() {
            return slideQuantity;
        }

    }

    public static String generateShowlyWebSite(int slideQuantity) {
        Handlebars handlebars = new Handlebars();
        try {
            Template template = handlebars.compile("templates/index");
            
            return template.apply(new PresentationData(slideQuantity));

        } catch (IOException e) {
            throw new RuntimeException("Error compiling the template.");
        }
    }
}
