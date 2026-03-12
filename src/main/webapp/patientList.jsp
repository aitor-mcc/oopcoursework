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
<h2>Patients:</h2>
  <%
    String errorMessage = (String) request.getAttribute("errorMessage");
    String selectedSort = (String) request.getAttribute("selectedSort");
    String filterCity = (String) request.getAttribute("filterCity");
    String filterState = (String) request.getAttribute("filterState");
    String filterEthnicity = (String) request.getAttribute("filterEthnicity");
    String filterRace = (String) request.getAttribute("filterRace");
    String filterGender = (String) request.getAttribute("filterGender");
    if (selectedSort == null) {
      selectedSort = "name-asc";
    }

    if (errorMessage != null)
    {
  %>
  <p style="color: red;"><%= errorMessage %></p>
  <%
    }

    if (filterCity != null) {
  %>
  <p>Showing patients in: <strong><%= filterCity %></strong> <a href="patientList">(View all patients)</a></p>
  <%
    } else if (filterState != null) {
  %>
  <p>Showing patients in: <strong><%= filterState %></strong> <a href="patientList">(View all patients)</a></p>
  <%
    } else if (filterEthnicity != null) {
  %>
  <p>Showing patients of ethnicity: <strong><%= filterEthnicity %></strong> <a href="patientList">(View all patients)</a></p>
  <%
    } else if (filterRace != null) {
  %>
  <p>Showing patients of race: <strong><%= filterRace %></strong> <a href="patientList">(View all patients)</a></p>
  <%
    } else if (filterGender != null) {
  %>
  <p>Showing patients of gender: <strong><%= filterGender %></strong> <a href="patientList">(View all patients)</a></p>
  <%
    }
  %>

  <form action="patientList" method="get">
    <%
      if (filterCity != null) {
    %>
    <input type="hidden" name="city" value="<%= filterCity %>">
    <%
      } else if (filterState != null) {
    %>
    <input type="hidden" name="state" value="<%= filterState %>">
    <%
      } else if (filterEthnicity != null) {
    %>
    <input type="hidden" name="ethnicity" value="<%= filterEthnicity %>">
    <%
      } else if (filterRace != null) {
    %>
    <input type="hidden" name="race" value="<%= filterRace %>">
    <%
      } else if (filterGender != null) {
    %>
    <input type="hidden" name="gender" value="<%= filterGender %>">
    <%
      }
    %>
    <label for="sort">Sort by:</label>
    <select name="sort" id="sort" onchange="this.form.submit()">
      <option value="name-asc" <%= "name-asc".equals(selectedSort) ? "selected" : "" %>>Alphabetical (A-Z)</option>
      <option value="name-desc" <%= "name-desc".equals(selectedSort) ? "selected" : "" %>>Alphabetical (Z-A)</option>
      <option value="age-desc" <%= "age-desc".equals(selectedSort) ? "selected" : "" %>>Age (Oldest First)</option>
      <option value="age-asc" <%= "age-asc".equals(selectedSort) ? "selected" : "" %>>Age (Youngest First)</option>
    </select>
    <noscript>
      <button type="submit">Sort</button>
    </noscript>
  </form>

  <ul>
    <%
      List<Patient> patients = (List<Patient>) request.getAttribute("patients");
      if (patients != null)
      {
        for (Patient patient : patients)
        {
          String href = "patient?id=" + URLEncoder.encode(patient.id(), "UTF-8");
    %>
    <li><a href="<%=href%>"><%=patient.name()%></a></li>
    <%  }
    }
    %>
</ul>
<jsp:include page="/footer.jsp"/>
</body>
</html>