package io.github.r0land013.showly;

import java.net.URL;

public class TestUtil {
    
    public static String getFilePath(String fileName) {
        URL url = TestUtil.class.getResource(fileName);
        return url.getPath();
    }

}
