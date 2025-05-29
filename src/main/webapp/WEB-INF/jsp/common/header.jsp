<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestion Billets Train - IIT</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <header>
        <div class="container-nav">
            <h1><a href="${pageContext.request.contextPath}/">SafeTrip</a></h1>
            <nav>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/">Accueil</a></li>
                    <li><a href="${pageContext.request.contextPath}/contact">Contact</a></li>
                    <c:choose>
                        <c:when test="${empty sessionScope.user}">
                            <li><a href="${pageContext.request.contextPath}/auth?action=showLogin">Connexion</a></li>
                            <li><a href="${pageContext.request.contextPath}/auth?action=showRegister">Inscription</a></li>
                        </c:when>
                        <c:otherwise>
                            <c:if test="${sessionScope.userRole == 'ADMIN'}">
                                <li><a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard Admin</a></li>
                            </c:if>
                            <c:if test="${sessionScope.userRole == 'CLIENT'}">
                                <li><a href="${pageContext.request.contextPath}/client/dashboard">Mon Espace</a></li>
                            </c:if>
                            <li><span>Bonjour, ${sessionScope.userName}</span></li>
                            <li><a href="${pageContext.request.contextPath}/auth?action=logout">Déconnexion</a></li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </nav>
        </div>
    </header>
    <main class="container">
