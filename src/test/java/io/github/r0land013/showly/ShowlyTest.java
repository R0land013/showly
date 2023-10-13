package io.github.r0land013.showly;

import org.junit.Test;
import io.github.r0land013.showly.slides.Slide;
import io.github.r0land013.showly.slides.exception.InvalidSlideFile;
import org.junit.Before;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.IOException;
import java.util.List;
import static io.github.r0land013.showly.TestConstants.SHOWLY_TEST_PORT;
import static io.github.r0land013.showly.TestConstants.SHOWLY_TEST_URL;
import static io.github.r0land013.showly.TestUtil.getFileInputStream;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShowlyTest {

    private Showly showlyServer;
    private OkHttpClient httpClient;

    @Before
    public void startHttpClient() {
        httpClient = new OkHttpClient();
    }

    @Test
    public void isShowlySeverRunning() throws IOException, InvalidSlideFile {

        ShowlyConfig config = new ShowlyConfig(SHOWLY_TEST_PORT, getFileInputStream("/presentation.pptx"));
        showlyServer = new Showly(config);
        showlyServer.show();

        Request mainPageRequest = new Request.Builder()
                .url(SHOWLY_TEST_URL)
                .build();

        try (Response response = httpClient.newCall(mainPageRequest).execute()) {
            assertTrue(response.isSuccessful());
        } catch (IOException ex) {
            fail();
        }

        showlyServer.stop();
    }

    @Test
    public void isServerStop() {

        Request mainPageRequest = new Request.Builder()
                .url(SHOWLY_TEST_URL)
                .build();

        try (Response response = httpClient.newCall(mainPageRequest).execute()) {
            fail();
        } catch (IOException ex) {
            // it could not connect to server so this test is successful
        }
    }

    @Test
    public void areSlidesExtractedFromXMLFileAfterShowlyIsStarted() throws IOException, InvalidSlideFile {
        ShowlyConfig config = new ShowlyConfig(SHOWLY_TEST_PORT, getFileInputStream("/presentation.pptx"));
        showlyServer = new Showly(config);
        List<Slide> extractedSlides = showlyServer.show();
        showlyServer.stop();
        
        XMLSlideShow pptx = new XMLSlideShow(getFileInputStream("/presentation.pptx"));
        List<XSLFSlide> slides = pptx.getSlides();
        
        assertTrue(slides.size() == extractedSlides.size());
    }
}
