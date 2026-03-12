<%@ page import="java.util.Map" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
  <jsp:include page="/meta.jsp"/>
  <title>Ethnicities - Patient Data App</title>
</head>
<body>
<jsp:include page="/header.jsp"/>

  <h2>View Patients by Ethnicity</h2>
  <%
    String errorMessage = (String) request.getAttribute("errorMessage");
    if (errorMessage != null)
    {
  %>
  <p style="color: red;"><%= errorMessage %></p>
  <%
    }
  %>

  <p>Select an ethnicity to view all patients of that ethnicity:</p>

  <ul>
    <%
      Map<String, Integer> ethnicitiesWithCounts = (Map<String, Integer>) request.getAttribute("ethnicitiesWithCounts");
      if (ethnicitiesWithCounts != null && !ethnicitiesWithCounts.isEmpty())
      {
        for (Map.Entry<String, Integer> entry : ethnicitiesWithCounts.entrySet())
        {
          String ethnicity = entry.getKey();
          Integer count = entry.getValue();
          String href = "patientList?ethnicity=" + URLEncoder.encode(ethnicity, "UTF-8");
    %>
    <li><a href="<%=href%>"><%=ethnicity%> (<%=count%> patient<%= count != 1 ? "s" : "" %>)</a></li>
    <%  }
      } else {
    %>
    <li>No ethnicities found in the data.</li>
    <%
      }
    %>
  </ul>

<jsp:include page="/footer.jsp"/>
</body>
</html>
