package uk.ac.ucl.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.Model;
import uk.ac.ucl.model.ModelFactory;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * EditPatientServlet handles adding, editing, and deleting patient rows.
 * It is mapped to the URL "/editPatient".
 * GET  /editPatient          → show the "Add New Patient" form
 * GET  /editPatient?id=...   → show the "Edit Patient" form pre-populated
 * POST /editPatient (action=add)    → add a new row and save CSV
 * POST /editPatient (action=edit)   → update an existing row and save CSV
 * POST /editPatient (action=delete) → delete a row and save CSV
 */
@WebServlet("/editPatient")
public class EditPatientServlet extends HttpServlet {

    /**
     * Handles GET requests.
     * Displays the add form (no id) or the edit form (id provided).
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Model model = ModelFactory.getModel();
            String id = request.getParameter("id");

            if (id != null && !id.isBlank()) {
                // Edit mode: load existing patient data
                Map<String, String> patientData = model.getPatientData(id);
                if (patientData == null) {
                    response.sendRedirect("patientList");
                    return;
                }
                request.setAttribute("patientData", patientData);
                request.setAttribute("editMode", true);
            } else {
                // Add mode: build an empty map with the correct column names
                Map<String, String> emptyData = new LinkedHashMap<>();
                for (String col : model.getColumnNames()) {
                    emptyData.put(col, "");
                }
                request.setAttribute("patientData", emptyData);
                request.setAttribute("editMode", false);
            }

            ServletContext context = getServletContext();
            RequestDispatcher dispatch = context.getRequestDispatcher("/editPatient.jsp");
            dispatch.forward(request, response);

        } catch (IOException e) {
            request.setAttribute("errorMessage", "Error loading data: " + e.getMessage());
            ServletContext context = getServletContext();
            RequestDispatcher dispatch = context.getRequestDispatcher("/error.jsp");
            dispatch.forward(request, response);
        }
    }

    /**
     * Handles POST requests for add, edit, and delete actions.
     * After any change the CSV file is re-written, then the user is
     * redirected to the patient list (PRG pattern prevents double-submission).
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Model model = ModelFactory.getModel();
            String action = request.getParameter("action");

            if ("delete".equals(action)) {
                String id = request.getParameter("id");
                if (id != null && !id.isBlank()) {
                    model.deletePatient(id);
                }
            } else {
                // Build a row data map from every column parameter
                Map<String, String> rowData = new LinkedHashMap<>();
                for (String col : model.getColumnNames()) {
                    String val = request.getParameter(col);
                    rowData.put(col, val != null ? val : "");
                }

                if ("edit".equals(action)) {
                    model.updatePatient(rowData.get("ID"), rowData);
                } else { // "add"
                    model.addPatient(rowData);
                }
            }

            // Persist changes to disk
            model.saveToCSV();

            // PRG: redirect so refreshing the page doesn't resubmit the form
            response.sendRedirect("patientList");

        } catch (IOException e) {
            request.setAttribute("errorMessage", "Error saving data: " + e.getMessage());
            ServletContext context = getServletContext();
            RequestDispatcher dispatch = context.getRequestDispatcher("/error.jsp");
            dispatch.forward(request, response);
        }
    }
}