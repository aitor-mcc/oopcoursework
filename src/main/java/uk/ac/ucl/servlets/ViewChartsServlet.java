package uk.ac.ucl.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.Model;
import uk.ac.ucl.model.ModelFactory;

import java.io.IOException;

@WebServlet("/charts")
public class ViewChartsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Model model = ModelFactory.getModel();

            request.setAttribute("ageDistribution",   model.getAgeDistribution());
            request.setAttribute("genderCounts",      model.getGendersWithCounts());
            request.setAttribute("raceCounts",        model.getRacesWithCounts());
            request.setAttribute("ethnicityCounts",   model.getEthnicitiesWithCounts());
            request.setAttribute("cityCounts",        model.getCitiesWithCounts());

            getServletContext()
                .getRequestDispatcher("/charts.jsp")
                .forward(request, response);

        } catch (IOException e) {
            request.setAttribute("errorMessage", "Error loading data: " + e.getMessage());
            getServletContext()
                .getRequestDispatcher("/error.jsp")
                .forward(request, response);
        }
    }
}
