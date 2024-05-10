package partA_Web_Service_API;

import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for F3 class.
 */
public class F3Test {
    private WireMockServer wireMockServer;
    private CloseableHttpClient httpClient;

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(8082); // Ensuring a unique port
        wireMockServer.start();
        WireMock.configureFor("localhost", 8082);
        httpClient = HttpClients.createDefault();
        F3.setBaseURL("http://localhost:8082");
    }

    @Test
    void testFindStaffWithMostAdmissions() {
        stubFor(get(urlEqualTo("/Allocations"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"employeeID\": 1}, {\"employeeID\": 1}, {\"employeeID\": 2}]")));

        String expected = "Staff ID 1 has the most admissions: 2";
        assertEquals(expected, F3.findStaffWithMostAdmissions());
    }

    @Test
    void testNoAdmissionsFound() {
        stubFor(get(urlEqualTo("/Allocations"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[]")));

        String expected = "No staff member has any admissions.";
        assertEquals(expected, F3.findStaffWithMostAdmissions());
    }

    @Test
    void testServerError() {
        stubFor(get(urlEqualTo("/Allocations"))
                .willReturn(aResponse()
                        .withStatus(500)));

        assertTrue(F3.findStaffWithMostAdmissions().startsWith("Error:"));
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }
}
