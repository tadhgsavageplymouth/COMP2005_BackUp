package partB_Frontend_App;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import partA_Web_Service_API.F1;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * The AdmissionDetailsUI class creates a graphical user interface to fetch and display admission details
 * for a specified patient ID in a tabular format.
 */
public class AdmissionDetailsUI extends JFrame {
    private JTextField patientIdField;
    private JTable resultTable;
    private JButton fetchButton;
    private DefaultTableModel tableModel;

    public AdmissionDetailsUI() {
        createUI();
    }

    private void createUI() {
        setTitle("Admission Details Fetcher");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        patientIdField = new JTextField(20);
        fetchButton = new JButton("Fetch Details");
        panel.add(new JLabel("Patient ID:"));
        panel.add(patientIdField);
        panel.add(fetchButton);
        add(panel, BorderLayout.NORTH);

        String[] columns = {"ID", "Admission Date", "Discharge Date", "Patient ID"};
        tableModel = new DefaultTableModel(columns, 0);
        resultTable = new JTable(tableModel);
        add(new JScrollPane(resultTable), BorderLayout.CENTER);

        fetchButton.addActionListener(e -> fetchAdmissionDetails(patientIdField.getText().trim()));
    }

    private void fetchAdmissionDetails(String patientId) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            String result = F1.getAdmissionsForPatient(patientId, client);
            if (result.trim().startsWith("[")) {
                updateTableWithJSONArray(result);
            } else if (result.trim().startsWith("{")) {
                updateTableWithJSONObject(result);
            } else {
                JOptionPane.showMessageDialog(this, "Received unexpected format: " + result, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to fetch data: " + ex.getMessage(), "Network Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTableWithJSONArray(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            Object[] rowData = new Object[4];
            tableModel.setRowCount(0); // Clear existing data

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                rowData[0] = obj.getInt("id");
                rowData[1] = obj.getString("admissionDate");
                rowData[2] = obj.optString("dischargeDate", "N/A");
                rowData[3] = obj.getInt("patientID");
                tableModel.addRow(rowData);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error parsing JSON data: " + e.getMessage(), "Data Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTableWithJSONObject(String jsonData) {
        try {
            JSONObject obj = new JSONObject(jsonData);
            Object[] rowData = new Object[4];
            rowData[0] = obj.getInt("id");
            rowData[1] = obj.getString("admissionDate");
            rowData[2] = obj.optString("dischargeDate", "N/A");
            rowData[3] = obj.optInt("patientID", -1);

            tableModel.setRowCount(0); // Clear existing data
            tableModel.addRow(rowData);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error parsing JSON data: " + e.getMessage(), "Data Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdmissionDetailsUI().setVisible(true));
    }
}
