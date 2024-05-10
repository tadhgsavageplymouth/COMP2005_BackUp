package partA_Web_Service_API;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * The F1 class is responsible for fetching admission details for a specific patient from the web service.
 */
public class F1 {
    private static String BASE_URL = "https://web.socem.plymouth.ac.uk/COMP2005/api";

    /**
     * Fetches the admission details for a given patient ID from the web service.
     * Handles different HTTP status codes and network exceptions.
     * @param patientId The unique identifier of the patient for whom the admission details are to be retrieved.
     * @param httpClient The HTTP client used to execute the web service call.
     * @return A string containing the JSON formatted details of the admission or an error message if the request fails.
     */
    public static String getAdmissionsForPatient(String patientId, CloseableHttpClient httpClient) {
        try {
            HttpGet request = new HttpGet(BASE_URL + "/Admissions/" + patientId);
            return httpClient.execute(request, httpResponse -> {
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                switch (statusCode) {
                    case 200:
                        return EntityUtils.toString(httpResponse.getEntity());
                    case 404:
                        return "Patient not found";
                    default:
                        return "Unexpected server error: " + statusCode;
                }
            });
        } catch (Exception e) {
            return "Network error: " + e.getMessage();
        }
    }

    public static void setBaseURL(String url) {
        BASE_URL = url;
    }
}
