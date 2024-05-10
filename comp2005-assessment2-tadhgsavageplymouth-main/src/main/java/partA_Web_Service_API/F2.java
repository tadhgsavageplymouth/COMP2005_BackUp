package partA_Web_Service_API;

import static spark.Spark.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class F2 {
    // Base URL of the COMP2005 API, utilised for fetching admission data.
    private static String BASE_URL = "https://web.socem.plymouth.ac.uk/COMP2005/api";

    // Sets the base URL to a specified URL, allowing for dynamic changes.
    public static void setBaseURL(String url) {
        BASE_URL = url;
    }

    // Main function setting up the server port and routes for the web service.
    public static void main(String[] args) {
        port(4567); // Set the server to run on port 4567

        // Route for fetching admissions details of a specific patient by their ID.
        get("/admissions/:id", (request, response) -> getAdmissionsForPatient(request.params(":id")));

        // Route for fetching current admissions without discharge dates.
        get("/admissions", (request, response) -> getCurrentAdmissions());
    }

    // Fetches admissions details for a specific patient using their unique ID.
    private static String getAdmissionsForPatient(String patientId) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL + "/Admissions/" + patientId);
            // Executes the HTTP GET request and returns the response as a string.
            return httpClient.execute(request, httpResponse -> EntityUtils.toString(httpResponse.getEntity()));
        } catch (Exception e) {
            // Returns a user-friendly error message if the fetching process fails.
            return "Error: " + e.getMessage();
        }
    }

    // Retrieves admissions that have not yet been discharged.
    public static String getCurrentAdmissions() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL + "/Admissions");
            String response = httpClient.execute(request, httpResponse -> EntityUtils.toString(httpResponse.getEntity()));
            JSONArray allAdmissions = new JSONArray(response);
            JSONArray nonDischargedAdmissions = new JSONArray();

            // Filters admissions to include only those without a discharge date.
            for (int i = 0; i < allAdmissions.length(); i++) {
                JSONObject admission = allAdmissions.getJSONObject(i);
                if (admission.optString("dischargeDate").isEmpty()) {
                    nonDischargedAdmissions.put(admission);
                }
            }
            // Returns the filtered admissions as a JSON array string.
            return nonDischargedAdmissions.toString();
        } catch (Exception e) {
            // Returns a user-friendly error message if there is an issue processing the JSON data.
            return "Error processing JSON: " + e.getMessage();
        }
    }
}
