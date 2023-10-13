package io.github.r0land013.showly.slides;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import io.github.r0land013.showly.slides.exception.InvalidSlideFile;
import org.apache.poi.ooxml.POIXMLException;

public class SlideExtractor {
    

    static public List<Slide> extractSlidesFromXMLSlides(FileInputStream slideFileStream) throws IOException, InvalidSlideFile {
        
        List<Slide> slideList = new LinkedList<Slide>();

        try {
            XMLSlideShow pptx = new XMLSlideShow(slideFileStream);
            
            List<XSLFSlide> xmlSlides = pptx.getSlides();
            Dimension pgsize = pptx.getPageSize();


            for (int slideIndex = 0; slideIndex < xmlSlides.size(); slideIndex++) {
                
                BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = img.createGraphics();

                // Clear the drawing area
                graphics.setPaint(Color.white);
                graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));

                // Render the slide onto the image
                xmlSlides.get(slideIndex).draw(graphics);

                slideList.add(new Slide(slideIndex, img));

                pptx.close();
            }
        } catch (IOException e) {
            throw e;
        }
        catch (POIXMLException e) {
            throw new InvalidSlideFile("Invalid XML slide file.");
        }

        return slideList;
    }

}
