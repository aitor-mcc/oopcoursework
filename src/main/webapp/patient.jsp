<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
  <jsp:include page="/meta.jsp"/>
  <title>Patient Details</title>
</head>
<body>
<jsp:include page="/header.jsp"/>

  <h2>Patient Details</h2>

  <%
    String errorMessage = (String) request.getAttribute("errorMessage");
    Map<String, String> patient = (Map<String, String>) request.getAttribute("patient");

    if (errorMessage != null) {
  %>
  <p style="color: red;"><%= errorMessage %></p>
  <%
  } else if (patient != null) {
  %>
  <table>
    <%
      for (Map.Entry<String, String> entry : patient.entrySet()) {
    %>
    <tr>
      <th><%= entry.getKey() %></th>
      <td><%= entry.getValue() %></td>
    </tr>
    <%
      }
    %>
  </table>
  <%
    }
  %>

  <%
    if (patient != null) {
      String editHref = "editPatient?id=" + patient.get("ID");
  %>
  <p>
    <a href="<%=editHref%>">Edit this patient</a>
  </p>
  <%
    }
  %>

  <p><a href="patientList">Back to patient list</a></p>

<jsp:include page="/footer.jsp"/>
</body>
</html>