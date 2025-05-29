<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<c:set var="isEdit" value="${not empty voyage.id}"/>
<c:set var="pageTitle" value="${isEdit ? 'Modifier le Voyage' : 'Ajouter un Voyage'}" scope="request"/>
<jsp:include page="/WEB-INF/jsp/admin/common/admin_header.jsp" />

<h2>${isEdit ? 'Modifier le Voyage' : 'Ajouter un Nouveau Voyage'}</h2>

<%-- Display error messages passed via request scope from validation --%>
<c:if test="${not empty errorMessage}">
    <p class="error-message">${errorMessage}</p>
</c:if>

<%-- The form submits to VoyageServlet --%>
<form action="${pageContext.request.contextPath}/admin/voyages" method="post">

    <%-- Hidden field to indicate the action (insert or update) --%>
    <input type="hidden" name="action" value="${formAction}">

    <%-- Hidden field for the ID when updating --%>
    <c:if test="${isEdit}">
        <input type="hidden" name="id" value="${voyage.id}">
    </c:if>

    <div class="form-group">
        <label for="trajetId">Trajet :</label>
        <select id="trajetId" name="trajetId" class="form-control" required>
            <option value="">-- Sélectionner un trajet --</option>
            <c:forEach var="trajet" items="${listTrajet}">
                <option value="${trajet.id}" ${voyage.trajet.id == trajet.id ? 'selected' : ''}>
                    <c:out value="${trajet.gareDepart.nom}"/> -> <c:out value="${trajet.gareArrivee.nom}"/>
                    <c:if test="${not empty trajet.description}"> (<c:out value="${trajet.description}"/>)</c:if>
                </option>
            </c:forEach>
        </select>
    </div>

    <div class="form-group">
        <label for="heureDepart">Heure de Départ :</label>
        <%-- Format LocalDateTime for datetime-local input which expects yyyy-MM-ddTHH:mm --%>
        <fmt:formatDate value="${voyage.heureDepart}" pattern="yyyy-MM-dd'T'HH:mm" var="heureDepartFormatted"/>
        <input type="datetime-local" id="heureDepart" name="heureDepart" class="form-control" 
               value="${heureDepartFormatted}" required>
    </div>

    <div class="form-group">
        <label for="heureArrivee">Heure d'Arrivée :</label>
        <fmt:formatDate value="${voyage.heureArrivee}" pattern="yyyy-MM-dd'T'HH:mm" var="heureArriveeFormatted"/>
        <input type="datetime-local" id="heureArrivee" name="heureArrivee" class="form-control" 
               value="${heureArriveeFormatted}" required>
    </div>

    <div class="form-group">
        <label for="prix">Prix :</label>
        <input type="number" id="prix" name="prix" class="form-control" 
               value="${voyage.prix}" required step="0.01" min="0">
    </div>

    <div class="form-group">
        <label for="placesDisponibles">Places Disponibles :</label>
        <input type="number" id="placesDisponibles" name="placesDisponibles" class="form-control" 
               value="${voyage.placesDisponibles}" required min="0">
    </div>

    <div class="form-group">
        <button type="submit" class="btn">${isEdit ? 'Mettre à jour' : 'Ajouter'}</button>
        <a href="${pageContext.request.contextPath}/admin/voyages?action=list" class="btn btn-secondary">Annuler</a>
    </div>

</form>

<jsp:include page="/WEB-INF/jsp/admin/common/admin_footer.jsp" />

