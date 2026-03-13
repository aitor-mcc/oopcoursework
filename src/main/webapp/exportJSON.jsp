<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
  <jsp:include page="/meta.jsp"/>
  <title>Export to JSON</title>
</head>
<body>
<jsp:include page="/header.jsp"/>

  <h2>Export Data to JSON</h2>

  <%
    String errorMessage = (String) request.getAttribute("errorMessage");
    String successMessage = (String) request.getAttribute("successMessage");
    String jsonData = (String) request.getAttribute("jsonData");

    if (errorMessage != null) {
  %>
  <p style="color: red;"><%= errorMessage %></p>
  <%
    } else if (successMessage != null) {
  %>
  <p style="color: green;"><%= successMessage %></p>
  <%
    }

    if (jsonData != null) {
  %>
  <h3>JSON Data:</h3>
  <textarea readonly style="width: 100%; height: 400px; font-family: monospace;"><%= jsonData %></textarea>
  
  <br><br>
  <form method="post" action="exportJSON">
    <input type="hidden" name="action" value="download">
    <button type="submit" class="link-button">Download as JSON File</button>
  </form>
  <%
    }
  %>

  <p><a href="index.html">Back to Home</a></p>

<jsp:include page="/footer.jsp"/>
</body>
</html>
