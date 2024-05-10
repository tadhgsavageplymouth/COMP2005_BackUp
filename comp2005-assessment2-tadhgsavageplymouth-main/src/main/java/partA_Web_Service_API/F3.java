package partA_Web_Service_API;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * The F3 class identifies the staff member who has overseen the most admissions.
 * It utilises the COMP2005 API to fetch allocation data.
 */
public class F3 {
    // The base URL for the API used to fetch allocation data.
    private static String BASE_URL = "https://web.socem.plymouth.ac.uk/COMP2005/api";

    // Method to set a different API base URL, allowing flexibility in server configuration.
    public static void setBaseURL(String url) {
        BASE_URL = url;
    }

    /**
     * Fetches and processes allocation data from the API to determine the staff member
     * with the highest number of admissions.
     *
     * @return A string detailing which staff member has the most admissions, or an error message.
     */
    public static String findStaffWithMostAdmissions() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL + "/Allocations");
            String response = httpClient.execute(request, httpResponse ->
                    EntityUtils.toString(httpResponse.getEntity()));
            return processAllocations(response);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Processes the JSON data from the API to calculate the number of admissions for each staff member.
     * It uses a HashMap to keep track of the counts and identifies the staff member with the most admissions.
     *
     * @param jsonData The JSON string containing allocation data.
     * @return A string indicating the staff member with the most admissions or a message if no admissions are found.
     */
    private static String processAllocations(String jsonData) {
        JSONArray allocations = new JSONArray(jsonData);
        Map<Integer, Integer> countMap = new HashMap<>();
        for (int i = 0; i < allocations.length(); i++) {
            JSONObject allocation = allocations.getJSONObject(i);
            int employeeId = allocation.getInt("employeeID");
            countMap.put(employeeId, countMap.getOrDefault(employeeId, 0) + 1);
        }

        int maxAdmissions = 0;
        int employeeWithMax = -1;
        for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() > maxAdmissions) {
                maxAdmissions = entry.getValue();
                employeeWithMax = entry.getKey();
            }
        }

        if (employeeWithMax == -1) {
            return "No staff member has any admissions.";
        }
        return "Staff ID " + employeeWithMax + " has the most admissions: " + maxAdmissions;
    }
}
