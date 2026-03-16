package uk.ac.ucl.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.Model;
import uk.ac.ucl.model.Patient;
import uk.ac.ucl.model.ModelFactory;

import java.io.IOException;
import java.util.List;

/**
 * The SearchServlet handles HTTP requests for performing patient searches.
 * It is mapped to the URL "/runsearch".
 */
@WebServlet("/runsearch")
public class SearchServlet extends HttpServlet {

  /**
   * Handles HTTP GET requests.
   * @param request  the HttpServletRequest object that contains the request the client has made of the servlet
   * @param response the HttpServletResponse object that contains the response the servlet sends to the client
   * @throws ServletException if the request for the GET could not be handled
   * @throws IOException      if an input or output error is detected when the servlet handles the GET request
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPost(request, response);
  }

  /**
   * Handles HTTP POST requests.
   * This is where the core search logic resides.
   *
   * @param request  the HttpServletRequest object that contains the request the client has made of the servlet
   * @param response the HttpServletResponse object that contains the response the servlet sends to the client
   * @throws ServletException if the request for the POST could not be handled
   * @throws IOException      if an input or output error is detected when the servlet handles the POST request
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // Retrieve the search term from the request parameter.
    String searchString = request.getParameter("searchstring");

    try {
      Model model = ModelFactory.getModel();

      // Basic validation of search input.
      if (searchString == null || searchString.trim().isEmpty()) {
        request.setAttribute("errorMessage", "Please enter a search term.");
      } else {
        List<Patient> searchResult = model.searchFor(searchString);
        request.setAttribute("result", searchResult);
      }

      ServletContext context = getServletContext();
      RequestDispatcher dispatch = context.getRequestDispatcher("/searchResult.jsp");
      dispatch.forward(request, response);

    } catch (IOException e) {
      request.setAttribute("errorMessage", "Error loading data: " + e.getMessage());
      ServletContext context = getServletContext();
      RequestDispatcher dispatch = context.getRequestDispatcher("/error.jsp");
      dispatch.forward(request, response);
    }
  }
}
