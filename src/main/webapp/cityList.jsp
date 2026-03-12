<%@ page import="java.util.Map" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
  <jsp:include page="/meta.jsp"/>
  <title>Cities - Patient Data App</title>
</head>
<body>
<jsp:include page="/header.jsp"/>
<h2>View Patients by City</h2>
  <%
    String errorMessage = (String) request.getAttribute("errorMessage");
    if (errorMessage != null)
    {
  %>
  <p style="color: red;"><%= errorMessage %></p>
  <%
    }
  %>

  <p>Select a city to view all patients living in that city:</p>

  <ul>
    <%
      Map<String, Integer> citiesWithCounts = (Map<String, Integer>) request.getAttribute("citiesWithCounts");
      if (citiesWithCounts != null && !citiesWithCounts.isEmpty())
      {
        for (Map.Entry<String, Integer> entry : citiesWithCounts.entrySet())
        {
          String city = entry.getKey();
          Integer count = entry.getValue();
          String href = "patientList?city=" + URLEncoder.encode(city, "UTF-8");
    %>
    <li><a href="<%=href%>"><%=city%> (<%=count%> resident<%= count != 1 ? "s" : "" %>)</a></li>
    <%  }
      } else {
    %>
    <li>No cities found in the data.</li>
    <%
      }
    %>
</ul>
<jsp:include page="/footer.jsp"/>
</body>
</html>
