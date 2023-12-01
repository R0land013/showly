package io.github.r0land013.showly.slides;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.LinkedList;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import io.github.r0land013.showly.slides.exception.InvalidSlideFileException;

import org.apache.poi.EmptyFileException;
import org.apache.poi.UnsupportedFileFormatException;
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.ooxml.POIXMLException;
import org.apache.poi.poifs.filesystem.NotOLE2FileException;

public class SlideExtractor {
    

    static private List<Slide> extractSlidesFromXMLSlides(String slideFilePath) throws IOException, InvalidSlideFileException {

        try {
            XMLSlideShow pptx = new XMLSlideShow(new FileInputStream(slideFilePath));
            
            return getSlidesFromXMLSlideFile(pptx);
        }
        
        catch (UnsupportedFileFormatException | EmptyFileException | POIXMLException | NotOLE2FileException e) {
            throw new InvalidSlideFileException("Invalid XML slide file.");
        }
        catch (IOException e) {
            throw e;
        }
    }

    static private List<Slide> getSlidesFromXMLSlideFile(XMLSlideShow pptx) throws IOException {
        List<Slide> slideList = new LinkedList<Slide>();
        
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
        return slideList;
    }

    static private List<Slide> extractSlidesFromBinarySlides(String slideFilePath) throws IOException, InvalidSlideFileException {

        try {
            
            HSLFSlideShow ppt = new HSLFSlideShow(new FileInputStream(slideFilePath));
            return getSlidesFromBinarySlideFile(ppt);
        }
        
        catch (UnsupportedFileFormatException | EmptyFileException | NotOLE2FileException e) {
            throw new InvalidSlideFileException("Invalid binary slide file.");
        }
        catch (IOException e) {
            throw e;
        }
    }

    static private List<Slide> getSlidesFromBinarySlideFile(HSLFSlideShow ppt) throws IOException {
        List<Slide> slideList = new LinkedList<Slide>();
        
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
        return slideList;
    }

    static public List<Slide> extractSlidesFromFile(String slideFilePath) throws IOException, InvalidSlideFileException {

        try {
            return extractSlidesFromXMLSlides(slideFilePath);
        }
        catch (InvalidSlideFileException e) {
            // The file is not xml slide file
        }
        
        try {
            return extractSlidesFromBinarySlides(slideFilePath);
        } catch (InvalidSlideFileException e) {
            throw new InvalidSlideFileException("This is not xml or binary slide file.");
        }
    }

    static public List<Slide> extractSlidesFromInputStream(InputStream inputStream) throws IOException, InvalidSlideFileException {
        
        // It is needed to read all data from the stream so it can be reused
        // because once an InputStream is read it can not be read from the
        // begining of that stream again.
        byte[] allBytesOfTheInputStream = inputStream.readAllBytes();
        
        try(ByteArrayInputStream byteArrayInpuStream = new ByteArrayInputStream(allBytesOfTheInputStream)) {
            return extractSlidesFromXMLInputStream(byteArrayInpuStream);
        }
        catch (InvalidSlideFileException e) {
            // The file is not xml slide file
            // Next, we will try to read the file as binary file.
        }
        
        try(ByteArrayInputStream byteArrayInpuStream = new ByteArrayInputStream(allBytesOfTheInputStream)) {

            return extractSlidesFromBinaryInputStream(byteArrayInpuStream);
        
        } catch (InvalidSlideFileException e) {
            throw new InvalidSlideFileException("This is not xml or binary slide file.");
        }
    }

    static private List<Slide> extractSlidesFromXMLInputStream(InputStream inputStream) throws InvalidSlideFileException, IOException {
        try {
            XMLSlideShow pptx = new XMLSlideShow(inputStream);
            
            return getSlidesFromXMLSlideFile(pptx);
        }
        
        catch (UnsupportedFileFormatException | EmptyFileException | POIXMLException | NotOLE2FileException e) {
            throw new InvalidSlideFileException("Invalid XML slide file.");
        }
        catch (IOException e) {
            throw e;
        }
    }

    static private List<Slide> extractSlidesFromBinaryInputStream(InputStream inputStream) throws InvalidSlideFileException, IOException {
        try {
            
            HSLFSlideShow ppt = new HSLFSlideShow(inputStream);
            return getSlidesFromBinarySlideFile(ppt);
        }
        
        catch (UnsupportedFileFormatException | EmptyFileException | NotOLE2FileException e) {
            throw new InvalidSlideFileException("This file is an XML based file.");
        }
        catch (IOException e) {
            throw e;
        }
    }

}
