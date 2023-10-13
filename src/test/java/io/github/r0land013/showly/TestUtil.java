package io.github.r0land013.showly;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;

public class TestUtil {
    
    public static FileInputStream getFileInputStream(String fileName) {
        URL url = TestUtil.class.getResource(fileName);
        try {
            return new FileInputStream(url.getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

}
