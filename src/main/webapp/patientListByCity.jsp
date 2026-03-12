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
<div class="main">
    <h2>Patients:</h2>
    <%
        String errorMessage = (String) request.getAttribute("errorMessage");
        String selectedSort = (String) request.getAttribute("selectedSort");
        if (selectedSort == null) {
            selectedSort = "name-asc";
        }

        if (errorMessage != null)
        {
    %>
    <p style="color: red;"><%= errorMessage %></p>
    <%
        }
    %>

    <form action="patientList" method="get">
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
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>