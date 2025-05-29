<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<%@ page import="java.time.LocalDateTime" %>

<%@ page import="java.time.format.DateTimeFormatter" %>



<c:set var="pageTitle" value="Dashboard Admin" scope="request"/>

<jsp:include page="/WEB-INF/jsp/admin/common/admin_header.jsp" />



<h2>Dashboard Principal</h2>

<p style="color: #666; font-size: 12px;">Dernière mise à jour : ${currentTime}</p>




<div class="dashboard-stats">

 <div class="stat-card success">

 <h4>Utilisateurs Actifs</h4>

 <div class="stat-value">${activeUsers}</div>

 <small>Utilisateurs non bannis</small>

 </div>

 <div class="stat-card warning">

 <h4>Réservations Aujourd'hui</h4>

 <div class="stat-value">${reservationsToday}</div>

 <small>Billets réservés aujourd'hui</small>

 </div>

 <div class="stat-card">

 <h4>Nombre de Billets</h4>

 <div class="stat-value">${totalBillets}</div>

 <small>Total des billets émis</small>

 </div>

 <div class="stat-card info">

 <h4>Nombre de Gares</h4>

 <div class="stat-value">${totalGares}</div>

 <small>Gares dans le système</small>

 </div>

 <div class="stat-card danger">

 <h4>Administrateurs</h4>

 <div class="stat-value">${adminCount}</div>

 <small>Comptes administrateurs</small>

 </div>

</div>




<div class="charts-section">

 <h3>Analyses et Tendances</h3>

 

 <div class="charts-container">


 <div class="chart-card">

 <h4>Répartition des Utilisateurs</h4>

 <div class="chart-tabs">

 <button class="tab-btn active" onclick="showUserChart('status')">Par Statut</button>

 <button class="tab-btn" onclick="showUserChart('roles')">Par Rôle</button>

 </div>

 <div class="chart-wrapper">

 <canvas id="userDistributionChart" width="300" height="300"></canvas>

 </div>

 </div>

 


 <div class="chart-card">

 <h4>Évolution des Réservations</h4>

 <p class="chart-subtitle">Nombre de réservations par mois (6 derniers mois)</p>

 <div class="chart-wrapper">

 <canvas id="reservationsTrendChart" width="400" height="200"></canvas>

 </div>

 </div>

 

 </div>

</div>




<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>



<script>


const userDistributionData = JSON.parse('${userDistributionData}');

const reservationsPerMonthData = JSON.parse('${reservationsPerMonthData}');




let userChart;

function createUserDistributionChart(dataType) {

 const ctx = document.getElementById('userDistributionChart').getContext('2d');

 

 if (userChart) {

 userChart.destroy();

 }

 

 const data = userDistributionData[dataType];

 

 userChart = new Chart(ctx, {

 type: 'doughnut',

 data: {

 labels: data.labels,

 datasets: [{

 data: data.data,

 backgroundColor: data.backgroundColor,

 borderWidth: 2,

 borderColor: '#fff'

 }]

 },

 options: {

 responsive: true,

 maintainAspectRatio: false,

 plugins: {

 legend: {

 position: 'bottom',

 labels: {

 padding: 20,

 usePointStyle: true

 }

 },

 tooltip: {

 callbacks: {

 label: function(context) {

 const total = context.dataset.data.reduce((a, b) => a + b, 0);

 const percentage = ((context.parsed * 100) / total).toFixed(1);

 return context.label + ': ' + context.parsed + ' (' + percentage + '%)';

 }

 }

 }

 }

 }

 });

}




const reservationsCtx = document.getElementById('reservationsTrendChart').getContext('2d');

new Chart(reservationsCtx, {

 type: 'line',

 data: {

 labels: reservationsPerMonthData.labels,

 datasets: [{

 label: 'Réservations',

 data: reservationsPerMonthData.data,

 borderColor: '#007bff',

 backgroundColor: 'rgba(0, 123, 255, 0.1)',

 borderWidth: 3,

 fill: true,

 tension: 0.4,

 pointBackgroundColor: '#007bff',

 pointBorderColor: '#fff',

 pointBorderWidth: 2,

 pointRadius: 6

 }]

 },

 options: {

 responsive: true,

 maintainAspectRatio: false,

 plugins: {

 legend: {

 display: false

 }

 },

 scales: {

 y: {

 beginAtZero: true,

 ticks: {

 stepSize: 1

 },

 grid: {

 color: 'rgba(0,0,0,0.1)'

 }

 },

 x: {

 grid: {

 display: false

 }

 }

 }

 }

});






function showUserChart(type) {


 document.querySelectorAll('.tab-btn').forEach(btn => btn.classList.remove('active'));

 event.target.classList.add('active');

 


 createUserDistributionChart(type);

}




createUserDistributionChart('status');

</script>




<style>

 .dashboard-stats {

 display: flex;

 flex-wrap: wrap;

 gap: 20px;

 margin-bottom: 40px;

 }

 

 .stat-card {

 flex: 1;

 min-width: 200px;

 padding: 20px;

 border-radius: 8px;

 background-color: #f8f9fa;

 box-shadow: 0 4px 6px rgba(0,0,0,0.1);

 text-align: center;

 transition: transform 0.3s ease, box-shadow 0.3s ease;

 position: relative;

 }

 

 .stat-card:hover {

 transform: translateY(-5px);

 box-shadow: 0 6px 12px rgba(0,0,0,0.15);

 }

 

 .stat-card h4 {

 margin-top: 0;

 margin-bottom: 10px;

 color: #495057;

 font-size: 16px;

 font-weight: 600;

 }

 

 .stat-card .stat-value {

 font-size: 32px;

 font-weight: bold;

 margin: 15px 0 10px 0;

 line-height: 1;

 }

 

 .stat-card small {

 color: #6c757d;

 font-size: 12px;

 display: block;

 margin-top: 5px;

 }

 

 .stat-card.success {

 background-color: #d4edda;

 color: #155724;

 border-left: 5px solid #28a745;

 }

 

 .stat-card.success .stat-value {

 color: #28a745;

 }

 

 .stat-card.warning {

 background-color: #fff3cd;

 color: #856404;

 border-left: 5px solid #ffc107;

 }

 

 .stat-card.warning .stat-value {

 color: #f57c00;

 }

 

 .stat-card.info {

 background-color: #d1ecf1;

 color: #0c5460;

 border-left: 5px solid #17a2b8;

 }

 

 .stat-card.info .stat-value {

 color: #17a2b8;

 }

 

 .stat-card.danger {

 background-color: #f8d7da;

 color: #721c24;

 border-left: 5px solid #dc3545;

 }

 

 .stat-card.danger .stat-value {

 color: #dc3545;

 }

 

 .stat-card:not(.success):not(.warning):not(.info):not(.danger) {

 background-color: #e9ecef;

 color: #495057;

 border-left: 5px solid #6c757d;

 }

 

 .stat-card:not(.success):not(.warning):not(.info):not(.danger) .stat-value {

 color: #495057;

 }

 

 /* Charts Section Styles */

 .charts-section {

 margin-top: 40px;

 }

 

 .charts-section h3 {

 color: #495057;

 margin-bottom: 30px;

 font-size: 24px;

 font-weight: 600;

 border-bottom: 2px solid #007bff;

 padding-bottom: 10px;

 }

 

 .charts-container {

 display: grid;

 grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));

 gap: 30px;

 margin-bottom: 40px;

 }

 

 .chart-card {

 background: #fff;

 border-radius: 12px;

 padding: 25px;

 box-shadow: 0 4px 12px rgba(0,0,0,0.1);

 transition: transform 0.3s ease, box-shadow 0.3s ease;

 }

 

 .chart-card:hover {

 transform: translateY(-2px);

 box-shadow: 0 8px 20px rgba(0,0,0,0.15);

 }

 

 .chart-card h4 {

 margin: 0 0 15px 0;

 color: #495057;

 font-size: 18px;

 font-weight: 600;

 }

 

 .chart-subtitle {

 color: #6c757d;

 font-size: 14px;

 margin: 0 0 20px 0;

 }

 

 .chart-wrapper {

 position: relative;

 height: 300px;

 margin-top: 20px;

 }

 

 .chart-tabs {

 display: flex;

 gap: 10px;

 margin-bottom: 20px;

 }

 

 .tab-btn {

 padding: 8px 16px;

 border: 2px solid #007bff;

 background: transparent;

 color: #007bff;

 border-radius: 20px;

 cursor: pointer;

 font-size: 14px;

 font-weight: 500;

 transition: all 0.3s ease;

 }

 

 .tab-btn:hover {

 background: #007bff;

 color: white;

 }

 

 .tab-btn.active {

 background: #007bff;

 color: white;

 }

 

 /* Responsive design */

 @media (max-width: 768px) {

 .dashboard-stats {

 flex-direction: column;

 }

 

 .stat-card {

 min-width: auto;

 }

 

 .charts-container {

 grid-template-columns: 1fr;

 }

 

 .chart-card {

 padding: 20px;

 }

 

 .chart-wrapper {

 height: 250px;

 }

 }

 

 @media (max-width: 480px) {

 .charts-container {

 gap: 20px;

 }

 

 .chart-card {

 padding: 15px;

 }

 

 .chart-wrapper {

 height: 200px;

 }

 }

</style>



<jsp:include page="/WEB-INF/jsp/admin/common/admin_footer.jsp" />