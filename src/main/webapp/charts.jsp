<%--
  Created by IntelliJ IDEA.
  User: aitor
  Date: 13/03/2026
  Time: 11:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <jsp:include page="/meta.jsp"/>
  <title>Charts</title>
  <!-- Chart.js from CDN — no installation needed -->
  <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
<jsp:include page="/header.jsp"/>

<h2>Data Charts</h2>

<%-- Helper: convert a Java Map to JS arrays inline --%>
<%
  // Build JS array strings for each chart
  Map<String, Integer> ageDist       = (Map<String, Integer>) request.getAttribute("ageDistribution");
  Map<String, Integer> genderCounts  = (Map<String, Integer>) request.getAttribute("genderCounts");
  Map<String, Integer> raceCounts    = (Map<String, Integer>) request.getAttribute("raceCounts");
  Map<String, Integer> ethCounts     = (Map<String, Integer>) request.getAttribute("ethnicityCounts");
  Map<String, Integer> cityCounts    = (Map<String, Integer>) request.getAttribute("cityCounts");
%>

<h3>Age Distribution</h3>
<canvas id="ageChart" width="600" height="300"></canvas>

<h3>Gender Breakdown</h3>
<canvas id="genderChart" width="400" height="400"></canvas>

<h3>Race Breakdown</h3>
<canvas id="raceChart" width="600" height="300"></canvas>

<h3>Ethnicity Breakdown</h3>
<canvas id="ethnicityChart" width="600" height="300"></canvas>

<h3>Patients per City (Top 20)</h3>
<canvas id="cityChart" width="700" height="350"></canvas>

<script>
  // Reusable helper to render a bar chart
  function barChart(id, labels, data, label, colour) {
    new Chart(document.getElementById(id), {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [{ label: label, data: data, backgroundColor: colour }]
      },
      options: { responsive: false, scales: { y: { beginAtZero: true } } }
    });
  }

  // Age Distribution
  barChart('ageChart',
    [<% for (String k : ageDist.keySet()) { %>'<%= k %>',<% } %>],
    [<% for (int v : ageDist.values()) { %><%= v %>,<% } %>],
    'Patients', 'rgba(54, 162, 235, 0.7)'
  );

  // Gender — pie chart
  new Chart(document.getElementById('genderChart'), {
    type: 'pie',
    data: {
      labels: [<% for (String k : genderCounts.keySet()) { %>'<%= k %>',<% } %>],
      datasets: [{
        data: [<% for (int v : genderCounts.values()) { %><%= v %>,<% } %>],
        backgroundColor: ['rgba(255,99,132,0.7)', 'rgba(54,162,235,0.7)', 'rgba(255,206,86,0.7)']
      }]
    },
    options: { responsive: false }
  });

  // Race
  barChart('raceChart',
    [<% for (String k : raceCounts.keySet()) { %>'<%= k %>',<% } %>],
    [<% for (int v : raceCounts.values()) { %><%= v %>,<% } %>],
    'Patients', 'rgba(255, 159, 64, 0.7)'
  );

  // Ethnicity
  barChart('ethnicityChart',
    [<% for (String k : ethCounts.keySet()) { %>'<%= k %>',<% } %>],
    [<% for (int v : ethCounts.values()) { %><%= v %>,<% } %>],
    'Patients', 'rgba(153, 102, 255, 0.7)'
  );

  // City — limit to top 20 by count
  barChart('cityChart',
    [<% int cityCount = 0; for (String k : cityCounts.keySet()) { if (cityCount++ >= 20) break; %>'<%= k %>',<% } %>],
    [<% cityCount = 0; for (int v : cityCounts.values()) { if (cityCount++ >= 20) break; %><%= v %>,<% } %>],
    'Patients', 'rgba(75, 192, 192, 0.7)'
  );
</script>

<p><a href="index.html">Back to Home</a></p>
<jsp:include page="/footer.jsp"/>
</body>
</html>
