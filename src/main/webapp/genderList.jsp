<%@ page import="java.util.Map" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
  <jsp:include page="/meta.jsp"/>
  <title>Genders - Patient Data App</title>
</head>
<body>
<jsp:include page="/header.jsp"/>

  <h2>View Patients by Gender</h2>
  <%
    String errorMessage = (String) request.getAttribute("errorMessage");
    if (errorMessage != null)
    {
  %>
  <p style="color: red;"><%= errorMessage %></p>
  <%
    }
  %>

  <p>Select a gender to view all patients of that gender:</p>

  <ul>
    <%
      Map<String, Integer> gendersWithCounts = (Map<String, Integer>) request.getAttribute("gendersWithCounts");
      if (gendersWithCounts != null && !gendersWithCounts.isEmpty())
      {
        for (Map.Entry<String, Integer> entry : gendersWithCounts.entrySet())
        {
          String gender = entry.getKey();
          Integer count = entry.getValue();
          String href = "patientList?gender=" + URLEncoder.encode(gender, "UTF-8");
    %>
    <li><a href="<%=href%>"><%=gender%> (<%=count%> patient<%= count != 1 ? "s" : "" %>)</a></li>
    <%  }
      } else {
    %>
    <li>No genders found in the data.</li>
    <%
      }
    %>
  </ul>

<jsp:include page="/footer.jsp"/>
</body>
</html>
