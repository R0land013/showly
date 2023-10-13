package io.github.r0land013.showly.slides;

import java.awt.image.BufferedImage;


public class Slide {
    
    private int slideIndex;
    private BufferedImage imageStream;

    public Slide(int slideIndex, BufferedImage imageStream) {
        this.slideIndex = slideIndex;
        this.imageStream = imageStream;
    }

    public int getSlideIndex() {
        return slideIndex;
    }

    public BufferedImage getImageStream() {
        return imageStream;
    }
}
