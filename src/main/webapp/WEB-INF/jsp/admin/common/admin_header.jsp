<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${not empty pageTitle ? pageTitle : 'Admin - Gestion Billets Train'} - IIT</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <!-- Include Chart.js from CDN -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
    <header>
        <div class="container-nav">
            <h1><a href="${pageContext.request.contextPath}/admin/dashboard">Admin SafeTrip </a></h1>
            <nav>
                <ul>
                    <c:if test="${not empty sessionScope.user}">
                         <li><span>Admin: ${sessionScope.userName}</span></li>
                         <li><a href="${pageContext.request.contextPath}/auth?action=logout">Déconnexion</a></li>
                    </c:if>
                    <c:if test="${empty sessionScope.user}">
                         <li><a href="${pageContext.request.contextPath}/auth?action=showLogin">Connexion Requise</a></li>
                    </c:if>
                     <li><a href="${pageContext.request.contextPath}/" target="_blank">Voir le site public</a></li>
                </ul>
            </nav>
        </div>
    </header>
    <div class="admin-wrapper">
        <jsp:include page="/WEB-INF/jsp/admin/common/admin_sidebar.jsp" />
        <main class="admin-content">

