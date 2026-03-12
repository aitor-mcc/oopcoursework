<%@ page import="java.util.Map" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
  <jsp:include page="/meta.jsp"/>
  <title>States - Patient Data App</title>
</head>
<body>
<jsp:include page="/header.jsp"/>

  <h2>View Patients by State</h2>
  <%
    String errorMessage = (String) request.getAttribute("errorMessage");
    if (errorMessage != null)
    {
  %>
  <p style="color: red;"><%= errorMessage %></p>
  <%
    }
  %>

  <p>Select a state to view all patients living in that state:</p>

  <ul>
    <%
      Map<String, Integer> statesWithCounts = (Map<String, Integer>) request.getAttribute("statesWithCounts");
      if (statesWithCounts != null && !statesWithCounts.isEmpty())
      {
        for (Map.Entry<String, Integer> entry : statesWithCounts.entrySet())
        {
          String state = entry.getKey();
          Integer count = entry.getValue();
          String href = "patientList?state=" + URLEncoder.encode(state, "UTF-8");
    %>
    <li><a href="<%=href%>"><%=state%> (<%=count%> resident<%= count != 1 ? "s" : "" %>)</a></li>
    <%  }
      } else {
    %>
    <li>No states found in the data.</li>
    <%
      }
    %>
  </ul>

<jsp:include page="/footer.jsp"/>
</body>
</html>
