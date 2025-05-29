<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.Duration" %>

<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<c:set var="pageTitle" value="Résultats de Recherche" scope="request"/>
<jsp:include page="/WEB-INF/jsp/common/header.jsp" />

<c:set var="dateFormatter" value="${DateTimeFormatter.ofPattern('dd/MM/yyyy')}" scope="page" />
<c:set var="timeFormatter" value="${DateTimeFormatter.ofPattern('HH:mm')}" scope="page" />

<h2>Résultats de Recherche de Voyages</h2>

<div class="search-summary">
    <p>Voyages trouvés pour le trajet <strong><c:out value="${gareDepartNom}"/></strong> 
    vers <strong><c:out value="${gareArriveeNom}"/></strong> 
    le <strong><c:out value="${dateRecherche.format(dateFormatter)}"/></strong>.</p>
    <a href="${pageContext.request.contextPath}/" class="btn btn-secondary btn-sm">Modifier la recherche</a>
</div>

<c:if test="${not empty sessionScope.searchError}">
    <p class="error-message">${sessionScope.searchError}</p>
    <c:remove var="searchError" scope="session" />
</c:if>
<c:if test="${not empty requestScope.errorMessage}">
    <p class="error-message">${requestScope.errorMessage}</p>
</c:if>

<div class="table-container results-table">
    <table>
        <thead>
            <tr>
                <th>Heure Départ</th>
                <th>Heure Arrivée</th>
                <th>Durée (estimée)</th>
                <th>Prix</th>
                <th>Places Disponibles</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${empty resultatsVoyage}">
                    <tr>
                        <td colspan="6" class="text-center">Aucun voyage direct trouvé pour les critères sélectionnés à cette date.</td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="voyage" items="${resultatsVoyage}">
                        <tr>
                            <td><c:out value="${voyage.heureDepart.format(timeFormatter)}"/></td>
                            <td><c:out value="${voyage.heureArrivee.format(timeFormatter)}"/></td>
                            <td>
                                <c:set var="duration" value="${Duration.between(voyage.heureDepart, voyage.heureArrivee)}" />
                                <c:set var="hours" value="${duration.toHours()}" />
                                <c:set var="minutes" value="${duration.toMinutesPart()}" />
                                <fmt:formatNumber value="${hours}" pattern="00"/>h <fmt:formatNumber value="${minutes}" pattern="00"/>m
                            </td>
                            <td><fmt:formatNumber value="${voyage.prix}" type="currency" currencySymbol="€"/></td>
                            <td>${voyage.placesDisponibles}</td>
                            <td class="actions">
                               
                                <c:choose>
                                    <c:when test="${empty sessionScope.user}">
                                        <a href="${pageContext.request.contextPath}/auth?action=showLogin&redirect=${pageContext.request.requestURI}?${pageContext.request.queryString}" class="btn btn-secondary btn-sm">Se connecter pour réserver</a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="${pageContext.request.contextPath}/reservation?action=confirm&voyageId=${voyage.id}" class="btn btn-primary btn-sm">Réserver</a>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>
</div>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp" />
