package partA_Web_Service_API;

import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the functionality of the F2 class which manages admissions queries from an API.
 */
public class F2Test {
    private WireMockServer wireMockServer;
    private CloseableHttpClient httpClient;

    /**
     * Prepares the testing environment, including setting up a mock server on a designated port.
     */
    @BeforeEach
    void setUp() {
        // Instantiate and start a WireMock server on port 8081.
        wireMockServer = new WireMockServer(8081);  // Using a unique port for F2 tests
        wireMockServer.start();
        WireMock.configureFor("localhost", 8081);
        httpClient = HttpClients.createDefault();
        F2.setBaseURL("http://localhost:8081");
    }

    /**
     * Verifies successful API call for getting current admissions without discharge dates.
     */
    @Test
    void testGetCurrentAdmissionsSuccess() {
        // Configure the WireMock server to simulate a successful response for current admissions.
        stubFor(get(urlEqualTo("/Admissions"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"id\":1,\"dischargeDate\":\"\"},{\"id\":3,\"dischargeDate\":\"\"}]")));

        String expected = "[{\"id\":1,\"dischargeDate\":\"\"},{\"id\":3,\"dischargeDate\":\"\"}]";
        assertEquals(expected, F2.getCurrentAdmissions());
    }

    /**
     * Tests the scenario where there are no current admissions to retrieve.
     */
    @Test
    void testGetCurrentAdmissionsEmpty() {
        // Configure the server to return an empty array indicating no current admissions.
        stubFor(get(urlEqualTo("/Admissions"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[]")));

        String expected = "[]";
        assertEquals(expected, F2.getCurrentAdmissions());
    }

    /**
     * Ensures that the system properly handles an internal server error from the API.
     */
    @Test
    void testServerError() {
        // Simulate a server error for the admissions endpoint.
        stubFor(get(urlEqualTo("/Admissions"))
                .willReturn(aResponse().withStatus(500)));

        assertTrue(F2.getCurrentAdmissions().startsWith("Error processing JSON"));
    }

    /**
     * Cleans up resources and ensures no mock server instances remain active after tests.
     */
    @AfterEach
    void tearDown() {
        // Stop the WireMock server if it is still running to clean up the test environment.
        if (wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }
}
