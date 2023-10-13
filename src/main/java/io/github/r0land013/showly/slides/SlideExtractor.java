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
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.ooxml.POIXMLException;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;

public class SlideExtractor {
    

    static private List<Slide> extractSlidesFromXMLSlides(String slideFilePath) throws IOException, InvalidSlideFile {
        
        List<Slide> slideList = new LinkedList<Slide>();

        try {
            XMLSlideShow pptx = new XMLSlideShow(new FileInputStream(slideFilePath));
            
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
            }

            pptx.close();
        }
        
        catch (OLE2NotOfficeXmlFileException e) {
            throw new InvalidSlideFile("Invalid XML slide file.");
        }
        catch (POIXMLException e) {
            throw new InvalidSlideFile("Invalid XML slide file.");
        }
        catch (IOException e) {
            throw e;
        }

        return slideList;
    }

    static private List<Slide> extractSlidesFromBinarySlides(String slideFilePath) throws IOException, InvalidSlideFile {
        
        List<Slide> slideList = new LinkedList<Slide>();

        try {
            HSLFSlideShow ppt = new HSLFSlideShow(new FileInputStream(slideFilePath));
            
            List<HSLFSlide> xmlSlides = ppt.getSlides();
            Dimension pgsize = ppt.getPageSize();


            for (int slideIndex = 0; slideIndex < xmlSlides.size(); slideIndex++) {
                
                BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = img.createGraphics();

                // Clear the drawing area
                graphics.setPaint(Color.white);
                graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));

                // Render the slide onto the image
                xmlSlides.get(slideIndex).draw(graphics);

                slideList.add(new Slide(slideIndex, img));

            }
            ppt.close();
        }
        
        catch (OfficeXmlFileException e) {
            throw new InvalidSlideFile("This file is an XML based file.");
        }
        catch (IOException e) {
            throw e;
        }

        return slideList;
    }

    static public List<Slide> extractSlidesFromFile(String slideFilePath) throws IOException, InvalidSlideFile {

        try {
            return extractSlidesFromXMLSlides(slideFilePath);
        }
        catch (InvalidSlideFile e) {
            // The file is not xml slide file
        }
        
        try {
            return extractSlidesFromBinarySlides(slideFilePath);
        } catch (InvalidSlideFile e) {
            throw new InvalidSlideFile("This is not xml or binary slide file.");
        }
        

    }

}
