<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<c:set var="pageTitle" value="Mon Espace Client" scope="request"/>
<jsp:include page="/WEB-INF/jsp/common/header.jsp" />

<h2>Mon Espace Client</h2>

<p>Bienvenue, <c:out value="${sessionScope.user.firstName}"/> <c:out value="${sessionScope.user.lastName}"/> !</p>
<div class="profile-link">
    <a href="${pageContext.request.contextPath}/profil" class="btn btn-secondary">
        <i class="profile-icon">👤</i> Gérer mon profil
    </a>
</div>

<c:if test="${not empty sessionScope.reservationSuccess}">
    <p class="success-message">${sessionScope.reservationSuccess}</p>
    <c:remove var="reservationSuccess" scope="session" />
</c:if>
<c:if test="${not empty sessionScope.reservationError}">
    <p class="error-message">${sessionScope.reservationError}</p>
    <c:remove var="reservationError" scope="session" />
</c:if>
<c:if test="${not empty sessionScope.cancelRequestSuccess}">
    <p class="success-message">${sessionScope.cancelRequestSuccess}</p>
    <c:remove var="cancelRequestSuccess" scope="session" />
</c:if>
<c:if test="${not empty sessionScope.cancelRequestError}">
    <p class="error-message">${sessionScope.cancelRequestError}</p>
    <c:remove var="cancelRequestError" scope="session" />
</c:if>


<h3>Mes Réservations</h3>

<div class="table-container reservations-table">
    <table>
        <thead>
            <tr>
                <th>Référence</th>
                <th>Date Réservation</th>
                <th>Départ</th>
                <th>Arrivée</th>
                <th>Date Voyage</th>
                <th>Heure Départ</th>
                <th>Prix Payé</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${empty mesBillets}">
                    <tr>
                        <td colspan="9" class="text-center">Vous n'avez aucune réservation pour le moment.</td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="billet" items="${mesBillets}">
                        <tr>
                            <td><c:out value="${billet.referenceBillet}"/></td>
                            <td>${billet.dateReservation.format(DateTimeFormatter.ofPattern('dd/MM/yyyy HH:mm'))}</td>
                            <td><c:out value="${billet.voyage.trajet.gareDepart.nom}"/></td>
                            <td><c:out value="${billet.voyage.trajet.gareArrivee.nom}"/></td>
                            <td>${billet.voyage.heureDepart.format(DateTimeFormatter.ofPattern('dd/MM/yyyy'))}</td>
                            <td>${billet.voyage.heureDepart.format(DateTimeFormatter.ofPattern('HH:mm'))}</td>
                            <td><fmt:formatNumber value="${billet.prixPaye}" type="currency" currencySymbol="€"/></td>
                     
                            <td class="actions">
                                <a href="${pageContext.request.contextPath}/billet?action=view&ref=${billet.referenceBillet}" class="btn btn-sm btn-info">Billet</a>
                                &nbsp;
                                
                                
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>
</div>



<jsp:include page="/WEB-INF/jsp/common/footer.jsp" />
