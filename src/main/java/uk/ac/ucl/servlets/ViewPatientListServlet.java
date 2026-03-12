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
import uk.ac.ucl.model.Patient;

import java.io.IOException;
import java.util.List;

/**
 * The ViewPatientListServlet handles HTTP requests for displaying the full list of patients.
 * It is mapped to the URL "/patientList".
 *
 * This servlet demonstrates:
 * 1. Handling GET requests to retrieve and display data.
 * 2. Interacting with a Model via a Factory pattern.
 * 3. Error handling and forwarding to error pages.
 * 4. Request-scoped attribute passing to JSPs for rendering lists.
 */
@WebServlet("/patientList")
public class ViewPatientListServlet extends HttpServlet
{

  /**
   * Handles HTTP GET requests.
   * This is the primary method for retrieving the patient list.
   *
   * @param request  the HttpServletRequest object that contains the request the client has made of the servlet
   * @param response the HttpServletResponse object that contains the response the servlet sends to the client
   * @throws ServletException if the request for the GET could not be handled
   * @throws IOException      if an input or output error is detected when the servlet handles the GET request
   */
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    try {
      Model model = ModelFactory.getModel();

      String sortBy = request.getParameter("sort");
      if (sortBy == null || sortBy.isBlank()) {
        sortBy = "name-asc";
      }

      String city = request.getParameter("city");
      String state = request.getParameter("state");
      String ethnicity = request.getParameter("ethnicity");
      String race = request.getParameter("race");
      String gender = request.getParameter("gender");
      List<Patient> patients;

      if (city != null && !city.isBlank()) {
        // Filter by city
        patients = model.getPatientsByCity(city, sortBy);
        request.setAttribute("filterCity", city);
      } else if (state != null && !state.isBlank()) {
        // Filter by state
        patients = model.getPatientsByState(state, sortBy);
        request.setAttribute("filterState", state);
      } else if (ethnicity != null && !ethnicity.isBlank()) {
        // Filter by ethnicity
        patients = model.getPatientsByEthnicity(ethnicity, sortBy);
        request.setAttribute("filterEthnicity", ethnicity);
      } else if (race != null && !race.isBlank()) {
        // Filter by race
        patients = model.getPatientsByRace(race, sortBy);
        request.setAttribute("filterRace", race);
      } else if (gender != null && !gender.isBlank()) {
        // Filter by gender
        patients = model.getPatientsByGender(gender, sortBy);
        request.setAttribute("filterGender", gender);
      } else {
        // Show all patients
        patients = model.getPatients(sortBy);
      }

      request.setAttribute("patients", patients);
      request.setAttribute("selectedSort", sortBy);

      ServletContext context = getServletContext();
      RequestDispatcher dispatch = context.getRequestDispatcher("/patientList.jsp");
      dispatch.forward(request, response);
    } catch (IOException e) {
      request.setAttribute("errorMessage", "Error loading data: " + e.getMessage());
      ServletContext context = getServletContext();
      RequestDispatcher dispatch = context.getRequestDispatcher("/error.jsp");
      dispatch.forward(request, response);
    }
  }

  /**
   * Handles HTTP POST requests.
   * Redirects to doGet as viewing a list is typically an idempotent operation.
   *
   * @param request  the HttpServletRequest object that contains the request the client has made of the servlet
   * @param response the HttpServletResponse object that contains the response the servlet sends to the client
   * @throws ServletException if the request for the POST could not be handled
   * @throws IOException      if an input or output error is detected when the servlet handles the POST request
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
  }
}