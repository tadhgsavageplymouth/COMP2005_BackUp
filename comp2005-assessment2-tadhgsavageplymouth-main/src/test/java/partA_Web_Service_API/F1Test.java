package partA_Web_Service_API;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A suite of tests for the F1 class, which is responsible for fetching admissions details from an API.
 */
public class F1Test {
    private WireMockServer wireMockServer;
    private CloseableHttpClient httpClient;

    /**
     * Sets up the test environment before each test, including the initialisation of a WireMock server
     * to simulate API responses without real network calls.
     */
    @BeforeEach
    void setUp() {
        // Initialising the WireMock server on a specific port.
        wireMockServer = new WireMockServer(8080);  // The port can be any that is available
        wireMockServer.start();
        WireMock.configureFor("localhost", 8080);

        // Creating a default HTTP client instance.
        httpClient = HttpClients.createDefault();

        // Setting the base URL of the F1 class to the mock server for testing.
        F1.setBaseURL("http://localhost:8080");
    }

    /**
     * Tests that a successful fetch of admission details returns the correct data.
     */
    @Test
    void testSuccessfulFetch() {
        // Configuring the mock server to return a typical response for a successful request.
        stubFor(get(urlEqualTo("/Admissions/123"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":123, \"admissionDate\":\"2024-01-01\", \"dischargeDate\":null}")));

        // Expected JSON response
        String expected = "{\"id\":123, \"admissionDate\":\"2024-01-01\", \"dischargeDate\":null}";
        // Verifying the response matches the expected JSON
        assertEquals(expected, F1.getAdmissionsForPatient("123", httpClient));
    }

    /**
     * Tests that the API correctly handles a situation where the patient ID does not exist.
     */
    @Test
    void testPatientNotFound() {
        // Configuring the mock server to return a 404 status.
        stubFor(get(urlEqualTo("/Admissions/999"))
                .willReturn(aResponse().withStatus(404)));

        // Verifying that the method returns the appropriate error message.
        assertEquals("Patient not found", F1.getAdmissionsForPatient("999", httpClient));
    }

    /**
     * Tests that the API response handler correctly processes a 500 server error.
     */
    @Test
    void testServerError() {
        // Simulating an internal server error.
        stubFor(get(urlEqualTo("/Admissions/123"))
                .willReturn(aResponse().withStatus(500)));

        // Confirming that the returned string starts with an error message indicating a server issue.
        assertTrue(F1.getAdmissionsForPatient("123", httpClient).startsWith("Unexpected server error"));
    }

    /**
     * Tests the resilience of the application to network errors.
     */
    @Test
    void testNetworkError() {
        // Simulate a network error by stopping the mock server.
        wireMockServer.stop();

        // Ensuring the method returns a message indicating a network error.
        assertTrue(F1.getAdmissionsForPatient("123", httpClient).startsWith("Network error"));
    }

    /**
     * Cleans up the test environment after each test, ensuring no side effects remain.
     */
    @AfterEach
    void tearDown() {
        // Stops the WireMock server if it is still running.
        if (wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }
}
