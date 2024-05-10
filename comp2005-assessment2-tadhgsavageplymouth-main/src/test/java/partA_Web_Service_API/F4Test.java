package partA_Web_Service_API;

import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for F4 class to ensure it correctly identifies staff with no admissions.
 */
public class F4Test {
    private WireMockServer wireMockServer;
    private CloseableHttpClient httpClient;

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(8083); // Unique port for testing
        wireMockServer.start();
        WireMock.configureFor("localhost", 8083);
        httpClient = HttpClients.createDefault();
        F4.setBaseURL("http://localhost:8083");
    }

    @Test
    void testListStaffWithNoAdmissions() {
        stubFor(get(urlEqualTo("/Employees"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"id\": 1}, {\"id\": 2}, {\"id\": 3}]")));

        stubFor(get(urlEqualTo("/Allocations"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"employeeID\": 1}, {\"employeeID\": 2}]")));

        String expected = "Staff with no admissions: [3]";
        assertEquals(expected, F4.listStaffWithNoAdmissions());
    }

    @Test
    void testAllStaffHaveAdmissions() {
        stubFor(get(urlEqualTo("/Employees"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"id\": 1}, {\"id\": 2}]")));

        stubFor(get(urlEqualTo("/Allocations"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"employeeID\": 1}, {\"employeeID\": 2}]")));

        String expected = "All staff have admissions.";
        assertEquals(expected, F4.listStaffWithNoAdmissions());
    }

    @Test
    void testServerError() {
        stubFor(get(urlEqualTo("/Employees"))
                .willReturn(aResponse().withStatus(500)));

        assertTrue(F4.listStaffWithNoAdmissions().startsWith("Error:"));
    }

    @AfterEach
    void tearDown() {
        if (wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }
}
