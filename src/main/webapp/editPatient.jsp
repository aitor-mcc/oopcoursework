<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="/meta.jsp"/>
    <title><%= Boolean.TRUE.equals(request.getAttribute("editMode")) ? "Edit Patient" : "Add Patient" %></title>
</head>
<body>
<jsp:include page="/header.jsp"/>

<%
    Map<String, String> patientData = (Map<String, String>) request.getAttribute("patientData");
    boolean editMode = Boolean.TRUE.equals(request.getAttribute("editMode"));
%>

<h2><%= editMode ? "Edit Patient" : "Add New Patient" %></h2>

<form action="editPatient" method="post">
    <input type="hidden" name="action" value="<%= editMode ? "edit" : "add" %>">

    <table>
        <%
            for (Map.Entry<String, String> entry : patientData.entrySet()) {
                String col = entry.getKey();
                String val = entry.getValue() == null ? "" : entry.getValue();
                boolean isId = "ID".equals(col);
        %>
        <tr>
            <td><label for="field_<%= col %>"><strong><%= col %></strong></label></td>
            <td>
                <input type="text"
                       id="field_<%= col %>"
                       name="<%= col %>"
                       value="<%= val %>"
                    <%= isId && editMode ? "readonly" : "" %>>
            </td>
        </tr>
        <% } %>
    </table>

    <br>
    <button type="submit" class="link-button"><%= editMode ? "Save Changes" : "Add Patient" %></button>
    <a href="patientList">Cancel</a>
</form>

<jsp:include page="/footer.jsp"/>
</body>
</html>