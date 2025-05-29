<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<c:set var="pageTitle" value="Gestion des Voyages" scope="request"/>
<jsp:include page="/WEB-INF/jsp/admin/common/admin_header.jsp" />

<%-- Définit le format de date/heure souhaité --%>
<c:set var="dateTimeFormatter" value="${DateTimeFormatter.ofPattern('dd/MM/yyyy HH:mm')}" scope="page" />

<h2>Liste des Voyages</h2>

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
    <a href="${pageContext.request.contextPath}/admin/voyages?action=new" class="btn">Ajouter un Nouveau Voyage</a>
</div>

<div class="table-container">
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Trajet (Départ - Arrivée)</th>
                <th>Heure Départ</th>
                <th>Heure Arrivée</th>
                <th>Prix</th>
                <th>Places Disponibles</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${empty listVoyage}">
                    <tr>
                        <td colspan="7" class="text-center">Aucun voyage trouvé.</td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="voyage" items="${listVoyage}">
                        <tr>
                            <td>${voyage.id}</td>
                            <td>
                                <c:out value="${voyage.trajet.gareDepart.nom}"/> - 
                                <c:out value="${voyage.trajet.gareArrivee.nom}"/>
                            </td>
                            <td><c:out value="${voyage.heureDepart.format(dateTimeFormatter)}"/></td>
                            <td><c:out value="${voyage.heureArrivee.format(dateTimeFormatter)}"/></td>
                            <td><fmt:formatNumber value="${voyage.prix}" type="currency" currencySymbol="€"/></td> <%-- Adjust currency symbol if needed --%>
                            <td>${voyage.placesDisponibles}</td>
                            <td class="actions">
    <a href="${pageContext.request.contextPath}/admin/voyages?action=edit&id=${voyage.id}" class="edit btn btn-sm btn-warning">Modifier</a>
    &nbsp;
    <!-- Nouveau bouton pour voir les voyageurs -->
    <a href="${pageContext.request.contextPath}/admin/voyageurs?voyageId=${voyage.id}" class="btn btn-sm btn-info">Voyageurs</a>
    &nbsp;
    <a href="${pageContext.request.contextPath}/admin/voyages?action=delete&id=${voyage.id}" 
       class="delete btn btn-sm btn-danger" 
       onclick="return confirm('Êtes-vous sûr de vouloir supprimer ce voyage ? Cette action est irréversible.\nAttention : Vérifiez qu\'aucun billet n\'est associé à ce voyage avant suppression.');">Supprimer</a>
</td>

                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>
</div>

<jsp:include page="/WEB-INF/jsp/admin/common/admin_footer.jsp" />
