<%@ page import="java.util.List" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="uk.ac.ucl.model.Patient" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
  <jsp:include page="/meta.jsp"/>
  <title>Patient Data App</title>
</head>
<body>
<jsp:include page="/header.jsp"/>

  <h1>Search Result</h1>
  <%
    String errorMessage = (String) request.getAttribute("errorMessage");
    List<Patient> patients = (List<Patient>) request.getAttribute("result");

    if (errorMessage != null)
    {
  %>
  <p style="color: red;"><%= errorMessage %></p>
  <%
  }
  else if (patients != null && !patients.isEmpty())
  {
  %>
  <ul>
    <%
      for (Patient patient : patients)
      {
        String href = "patient?id=" + URLEncoder.encode(patient.id(), "UTF-8");
    %>
    <li><a href="<%= href %>"><%= patient.name() %></a></li>
    <%
      }
    %>
  </ul>
  <%
  }
  else
  {
  %>
  <p>Nothing found</p>
  <%
    }
  %>

<jsp:include page="/footer.jsp"/>
</body>
</html>