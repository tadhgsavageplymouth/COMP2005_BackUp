package partA_Web_Service_API;

import static spark.Spark.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class App {
    private static final String BASE_URL = "https://web.socem.plymouth.ac.uk/COMP2005/api";

    public static void main(String[] args) {
        port(4567); // Set the port on which the application will run

        // Define the endpoint to retrieve details for a specific patient's admission
        get("/admissions/:id", (request, response) -> getAdmissionsForPatient(request.params(":id")));

        // Define the endpoint to list all admissions
        get("/admissions", (request, response) -> getCurrentAdmissions());

        // Define the endpoint to find the staff member with the most admissions
        get("/staff/most-admissions", (request, response) -> F3.findStaffWithMostAdmissions());

        // Define the endpoint to list staff members who have no admissions
        get("/staff/no-admissions", (request, response) -> F4.listStaffWithNoAdmissions());
    }

    /**
     * Fetches detailed admission information for a specific patient using their ID.
     * @param patientId The unique identifier of the patient.
     * @return A string containing the admission details or an error message.
     */
    private static String getAdmissionsForPatient(String patientId) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL + "/Admissions/" + patientId);
            String response = httpClient.execute(request, httpResponse ->
                    EntityUtils.toString(httpResponse.getEntity()));
            return response;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Retrieves a list of all current admissions where patients have not been discharged.
     * @return A string representation of all non-discharged admissions in JSON format.
     */
    public static String getCurrentAdmissions() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL + "/Admissions");
            String response = httpClient.execute(request, httpResponse ->
                    EntityUtils.toString(httpResponse.getEntity()));

            JSONArray allAdmissions = new JSONArray(response);
            JSONArray nonDischargedAdmissions = new JSONArray();
            for (int i = 0; i < allAdmissions.length(); i++) {
                JSONObject admission = allAdmissions.getJSONObject(i);
                if (admission.optString("dischargeDate").isEmpty()) {
                    nonDischargedAdmissions.put(admission);
                }
            }
            return nonDischargedAdmissions.toString();
        } catch (Exception e) {
            return "Error processing JSON: " + e.getMessage();
        }
    }
}
