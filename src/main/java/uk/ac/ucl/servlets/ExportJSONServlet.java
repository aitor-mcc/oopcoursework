package uk.ac.ucl.servlets;

import uk.ac.ucl.model.Model;
import uk.ac.ucl.model.ModelFactory;
import uk.ac.ucl.model.JSONWriter;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet that exports the patient data to JSON format.
 * GET: Displays JSON data in a JSP page with option to download
 * POST: Downloads JSON data as a file
 */
@WebServlet("/exportJSON")
public class ExportJSONServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Model model = ModelFactory.getModel();
            JSONWriter jsonWriter = new JSONWriter();

            // Get the JSON string
            String json = jsonWriter.toJSON(model.getDataFrame());

            // Set attributes for JSP
            request.setAttribute("jsonData", json);
            request.setAttribute("successMessage", "JSON data generated successfully. You can view it below or download it as a file.");

            // Forward to JSP
            RequestDispatcher dispatcher = request.getRequestDispatcher("/exportJSON.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error generating JSON: " + e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/exportJSON.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("download".equals(action)) {
            try {
                Model model = ModelFactory.getModel();
                JSONWriter jsonWriter = new JSONWriter();

                // Get the JSON string
                String json = jsonWriter.toJSON(model.getDataFrame());

                // Set response type to JSON for download
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Disposition", "attachment; filename=\"patients.json\"");

                // Write JSON to response
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();

            } catch (Exception e) {
                request.setAttribute("errorMessage", "Error downloading JSON: " + e.getMessage());
                RequestDispatcher dispatcher = request.getRequestDispatcher("/exportJSON.jsp");
                dispatcher.forward(request, response);
            }
        }
    }
}
