package partA_Web_Service_API;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

/**
 * The F4 class is designed to identify staff members who have not been allocated any admissions.
 * It interacts with the COMP2005 API to fetch data related to employees and their allocations.
 */
public class F4 {
    // The base URL for the COMP2005 API, utilised to fetch employee and allocation data.
    private static String BASE_URL = "https://web.socem.plymouth.ac.uk/COMP2005/api";

    /**
     * Allows the API base URL to be updated dynamically, facilitating testing or deployment changes.
     *
     * @param url The new base URL to be used for API requests.
     */
    public static void setBaseURL(String url) {
        BASE_URL = url;
    }

    /**
     * Retrieves a list of staff members who currently have no admissions assigned to them.
     * It first fetches all employees, then removes those who appear in the allocations list.
     *
     * @return A string listing the IDs of staff with no admissions or a confirmation that all staff have admissions.
     */
    public static String listStaffWithNoAdmissions() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // Fetches a list of all employees from the API.
            HttpGet request = new HttpGet(BASE_URL + "/Employees");
            String employeeResponse = httpClient.execute(request, httpResponse ->
                    EntityUtils.toString(httpResponse.getEntity()));
            JSONArray employees = new JSONArray(employeeResponse);

            // Stores employee IDs in a set for easy removal.
            Set<Integer> employeeIDs = new HashSet<>();
            for (int i = 0; i < employees.length(); i++) {
                JSONObject employee = employees.getJSONObject(i);
                employeeIDs.add(employee.getInt("id"));
            }

            // Fetches a list of all allocations to check which employees have admissions assigned.
            request = new HttpGet(BASE_URL + "/Allocations");
            String allocationResponse = httpClient.execute(request, httpResponse ->
                    EntityUtils.toString(httpResponse.getEntity()));
            JSONArray allocations = new JSONArray(allocationResponse);

            // Removes employees from the set who are listed in the allocations.
            for (int i = 0; i < allocations.length(); i++) {
                JSONObject allocation = allocations.getJSONObject(i);
                employeeIDs.remove(allocation.getInt("employeeID"));
            }

            // Returns a list of employee IDs with no admissions or confirms that all staff have admissions.
            return employeeIDs.isEmpty() ? "All staff have admissions." :
                    "Staff with no admissions: " + employeeIDs.toString();
        } catch (Exception e) {
            // Returns an error message if the process encounters an issue.
            return "Error: " + e.getMessage();
        }
    }
}
