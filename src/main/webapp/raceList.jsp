<%@ page import="java.util.Map" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
  <jsp:include page="/meta.jsp"/>
  <title>Races - Patient Data App</title>
</head>
<body>
<jsp:include page="/header.jsp"/>

  <h2>View Patients by Race</h2>
  <%
    String errorMessage = (String) request.getAttribute("errorMessage");
    if (errorMessage != null)
    {
  %>
  <p style="color: red;"><%= errorMessage %></p>
  <%
    }
  %>

  <p>Select a race to view all patients of that race:</p>

  <ul>
    <%
      Map<String, Integer> racesWithCounts = (Map<String, Integer>) request.getAttribute("racesWithCounts");
      if (racesWithCounts != null && !racesWithCounts.isEmpty())
      {
        for (Map.Entry<String, Integer> entry : racesWithCounts.entrySet())
        {
          String race = entry.getKey();
          Integer count = entry.getValue();
          String href = "patientList?race=" + URLEncoder.encode(race, "UTF-8");
    %>
    <li><a href="<%=href%>"><%=race%> (<%=count%> patient<%= count != 1 ? "s" : "" %>)</a></li>
    <%  }
      } else {
    %>
    <li>No races found in the data.</li>
    <%
      }
    %>
  </ul>

<jsp:include page="/footer.jsp"/>
</body>
</html>
