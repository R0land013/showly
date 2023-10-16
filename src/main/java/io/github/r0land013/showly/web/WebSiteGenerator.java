package io.github.r0land013.showly.web;

import java.io.IOException;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;

public class WebSiteGenerator {
    
    private static class PresentationData {
        
        private int slideQuantity;
        private String presentationName;

        public PresentationData(int slideQuantity, String presentationName) {
            this.slideQuantity = slideQuantity;
            this.presentationName = presentationName;
        }

        public int getSlideQuantity() {
            return slideQuantity;
        }

        public String getPresentationName() {
            return presentationName;
        }

    }

    public static String generateShowlyWebSite(int slideQuantity, String presentationName) {
        Handlebars handlebars = new Handlebars();
        try {
            Template template = handlebars.compile("templates/index");
            
            return template.apply(new PresentationData(slideQuantity, presentationName));

        } catch (IOException e) {
            throw new RuntimeException("Error compiling the template.");
        }
    }
}
