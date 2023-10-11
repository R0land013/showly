package io.github.r0land013.showly;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import static io.github.r0land013.showly.TestConstants.SHOWLY_TEST_PORT;
import static io.github.r0land013.showly.TestConstants.SHOWLY_TEST_URL;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShowlyTest {

    private Showly showlyServer;
    private OkHttpClient httpClient;

    @Before
    public void startShowlyServer() {
        ShowlyConfig config = ShowlyConfig.create(SHOWLY_TEST_PORT);
        showlyServer = new Showly(config);
        showlyServer.show();

        httpClient = new OkHttpClient();
    }
    
    @After
    public void stopShowlyServer() {
        showlyServer.stop();
    }

    @Test
    public void isTheSeverRunning() throws IOException {
        
        Request mainPageRequest = new Request.Builder()
        .url(SHOWLY_TEST_URL)
        .build();

        try (Response response = httpClient.newCall(mainPageRequest).execute()) {
            assertTrue(response.isSuccessful());
        }
        catch(IOException ex) {
            fail();
        }
    }

    @Test
    public void isServerStop() {
        showlyServer.stop();

        Request mainPageRequest = new Request.Builder()
        .url(SHOWLY_TEST_URL)
        .build();

        try (Response response = httpClient.newCall(mainPageRequest).execute()) {
            fail();
        }
        catch(IOException ex) {
            // it could not connect to server so this test is successful
        }
    }
}
