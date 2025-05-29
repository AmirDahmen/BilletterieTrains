<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<c:set var="pageTitle" value="Liste des Voyageurs" scope="request"/>
<jsp:include page="/WEB-INF/jsp/admin/common/admin_header.jsp" />

<%-- Définit le format de date/heure souhaité --%>
<c:set var="dateTimeFormatter" value="${DateTimeFormatter.ofPattern('dd/MM/yyyy HH:mm')}" scope="page" />

<h2>Voyageurs pour le voyage #${voyage.id}</h2>
<h3>
    <c:out value="${voyage.trajet.gareDepart.nom}"/> - 
    <c:out value="${voyage.trajet.gareArrivee.nom}"/>
    (<c:out value="${voyage.heureDepart.format(dateTimeFormatter)}"/>)
</h3>

<%-- Display success or error messages passed via session --%>
<c:if test="${not empty sessionScope.successMessage}">
    <p class="success-message">${sessionScope.successMessage}</p>
    <c:remove var="successMessage" scope="session" />
</c:if>
<c:if test="${not empty sessionScope.errorMessage}">
    <p class="error-message">${sessionScope.errorMessage}</p>
    <c:remove var="errorMessage" scope="session" />
</c:if>
<c:if test="${not empty requestScope.errorMessage}">
    <p class="error-message">${requestScope.errorMessage}</p>
</c:if>

<div class="mb-20">
    <a href="${pageContext.request.contextPath}/admin/voyages" class="btn">Retour à la liste des voyages</a>
</div>

<div class="table-container">
    <table>
        <thead>
            <tr>
                <th>ID Billet</th>
                <th>Référence</th>
                <th>Voyageur</th>
                <th>Email</th>
                <th>Date Réservation</th>
                <th>Prix Payé</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${empty billets}">
                    <tr>
                        <td colspan="7" class="text-center">Aucun voyageur trouvé pour ce voyage.</td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="billet" items="${billets}">
                        <tr>
                            <td>${billet.id}</td>
                            <td><c:out value="${billet.referenceBillet}"/></td>
                            <td>
                                <c:out value="${billet.user.firstName}"/> 
                                <c:out value="${billet.user.lastName}"/>
                            </td>
                            <td><c:out value="${billet.user.email}"/></td>
                            <td><c:out value="${billet.dateReservation.format(dateTimeFormatter)}"/></td>
                            <td><fmt:formatNumber value="${billet.prixPaye}" type="currency" currencySymbol="€"/></td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>
</div>

<jsp:include page="/WEB-INF/jsp/admin/common/admin_footer.jsp" />
