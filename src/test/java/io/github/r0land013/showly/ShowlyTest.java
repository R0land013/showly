package io.github.r0land013.showly;

import org.junit.Test;
import io.github.r0land013.showly.slides.Slide;
import io.github.r0land013.showly.slides.exception.InvalidSlideFileException;
import org.junit.Before;
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import static io.github.r0land013.showly.TestConstants.SHOWLY_TEST_PORT;
import static io.github.r0land013.showly.TestConstants.SHOWLY_TEST_URL;
import static io.github.r0land013.showly.TestUtil.getFilePath;
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
    public void isShowlySeverRunning() throws IOException, InvalidSlideFileException {

        ShowlyConfig config = new ShowlyConfig(SHOWLY_TEST_PORT, getFilePath("/presentation.pptx"));
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
    public void areSlidesExtractedFromXMLFileAfterShowlyIsStarted() throws IOException, InvalidSlideFileException {
        ShowlyConfig config = new ShowlyConfig(SHOWLY_TEST_PORT, getFilePath("/presentation.pptx"));
        showlyServer = new Showly(config);
        List<Slide> extractedSlides = showlyServer.show();
        showlyServer.stop();

        XMLSlideShow pptx = new XMLSlideShow(new FileInputStream(getFilePath("/presentation.pptx")));
        List<XSLFSlide> slides = pptx.getSlides();

        assertTrue(slides.size() == extractedSlides.size());
    }

    @Test
    public void areSlidesExtractedFromBinaryFileAfterShowlyIsStarted() throws IOException, InvalidSlideFileException {
        ShowlyConfig config = new ShowlyConfig(SHOWLY_TEST_PORT, getFilePath("/binary.ppt"));
        showlyServer = new Showly(config);
        List<Slide> extractedSlides = showlyServer.show();
        showlyServer.stop();

        HSLFSlideShow ppt = new HSLFSlideShow(new FileInputStream(getFilePath("/binary.ppt")));
        List<HSLFSlide> slides = ppt.getSlides();

        assertTrue(slides.size() == extractedSlides.size());
    }

    @Test
    public void extractingSlidesFromNonSlideFileThrowsException() throws IOException {
        ShowlyConfig config = new ShowlyConfig(SHOWLY_TEST_PORT, getFilePath("/document.docx"));
        showlyServer = new Showly(config);

        try {
            showlyServer.show();
            showlyServer.stop();
            fail("InvalidSlideFileException not thrown");
        } catch (InvalidSlideFileException e) {
            // success
        }
    }

    @Test
    public void extractingSlidesFromNonExistentFileThrowsException() throws InvalidSlideFileException {
        ShowlyConfig config = new ShowlyConfig(SHOWLY_TEST_PORT, "./non_existent_file.ppt");
        showlyServer = new Showly(config);

        try {
            showlyServer.show();
            showlyServer.stop();
            fail("IOException not thrown");
        } catch (IOException e) {
            // success
        }
    }

    @Test
    public void slidesAsImagesCanBeRetrievedUsingHttpRequests() throws IOException, InvalidSlideFileException {
        ShowlyConfig config = new ShowlyConfig(SHOWLY_TEST_PORT, getFilePath("/binary.ppt"));
        showlyServer = new Showly(config);
        List<Slide> extractedSlides = showlyServer.show();

        for (int slideIndex = 0; slideIndex < extractedSlides.size(); slideIndex++) {
            
            // Example: http://localhost:8080/slides/0
            String url = String.format("%s/slides/%d", SHOWLY_TEST_URL, slideIndex);
            
            if(!isSuccessfulARequestTo(url)) {
                showlyServer.stop();
                fail("Requesting to " + url + " was not successful.");
                break;
            }
        }
        showlyServer.stop();

    }

    private boolean isSuccessfulARequestTo(String url) {
        Request mainPageRequest = new Request.Builder()
                .url(url)
                .build();

        try (Response response = httpClient.newCall(mainPageRequest).execute()) {
            String contentType = response.header("Content-Type");
            return response.isSuccessful() && contentType.equals("image/png");

        } catch (IOException ex) {
            return false;
        }
    }
}
