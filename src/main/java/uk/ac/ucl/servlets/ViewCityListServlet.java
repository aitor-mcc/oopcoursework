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
import java.util.Map;

/**
 * The ViewCityListServlet handles HTTP requests for displaying a list of all cities
 * where patients live, along with the count of residents in each city.
 * It is mapped to the URL "/cityList".
 *
 * This servlet demonstrates:
 * 1. Handling GET requests to retrieve and display unique cities with counts.
 * 2. Interacting with a Model via a Factory pattern.
 * 3. Error handling and forwarding to error pages.
 * 4. Request-scoped attribute passing to JSPs for rendering lists.
 */
@WebServlet("/cityList")
public class ViewCityListServlet extends HttpServlet {

  /**
   * Handles HTTP GET requests.
   * Retrieves all unique cities with their resident counts from the model
   * and forwards to the city list JSP.
   *
   * @param request  the HttpServletRequest object that contains the request the client has made of the servlet
   * @param response the HttpServletResponse object that contains the response the servlet sends to the client
   * @throws ServletException if the request for the GET could not be handled
   * @throws IOException      if an input or output error is detected when the servlet handles the GET request
   */
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    try {
      Model model = ModelFactory.getModel();

      Map<String, Integer> citiesWithCounts = model.getCitiesWithCounts();

      request.setAttribute("citiesWithCounts", citiesWithCounts);

      ServletContext context = getServletContext();
      RequestDispatcher dispatch = context.getRequestDispatcher("/cityList.jsp");
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
