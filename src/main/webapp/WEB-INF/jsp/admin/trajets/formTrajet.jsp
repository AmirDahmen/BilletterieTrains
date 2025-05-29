<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<c:set var="isEdit" value="${not empty trajet.id}"/>
<c:set var="pageTitle" value="${isEdit ? 'Modifier le Trajet' : 'Ajouter un Trajet'}" scope="request"/>
<jsp:include page="/WEB-INF/jsp/admin/common/admin_header.jsp" />

<h2>${isEdit ? 'Modifier le Trajet' : 'Ajouter un Nouveau Trajet'}</h2>

<c:if test="${not empty errorMessage}">
    <p class="error-message">${errorMessage}</p>
</c:if>

<form action="${pageContext.request.contextPath}/admin/trajets" method="post">

    <input type="hidden" name="action" value="${formAction}">

    <c:if test="${isEdit}">
        <input type="hidden" name="id" value="${trajet.id}">
    </c:if>

    <div class="form-group">
        <label for="gareDepartId">Gare de Départ :</label>
        <select id="gareDepartId" name="gareDepartId" class="form-control" required>
            <option value="">-- Sélectionner une gare --</option>
            <c:forEach var="gare" items="${listGare}">
                <option value="${gare.id}" ${trajet.gareDepart.id == gare.id ? 'selected' : ''}>
                    <c:out value="${gare.nom}"/> (<c:out value="${gare.ville}"/>)
                </option>
            </c:forEach>
        </select>
    </div>

    <div class="form-group">
        <label for="gareArriveeId">Gare d'Arrivée :</label>
        <select id="gareArriveeId" name="gareArriveeId" class="form-control" required>
            <option value="">-- Sélectionner une gare --</option>
             <c:forEach var="gare" items="${listGare}">
                <option value="${gare.id}" ${trajet.gareArrivee.id == gare.id ? 'selected' : ''}>
                    <c:out value="${gare.nom}"/> (<c:out value="${gare.ville}"/>)
                </option>
            </c:forEach>
        </select>
    </div>

    <div class="form-group">
        <label for="description">Description (Optionnel) :</label>
        <input type="text" id="description" name="description" class="form-control" 
               value="<c:out value='${trajet.description}'/>">
    </div>

    <div class="form-group">
        <button type="submit" class="btn">${isEdit ? 'Mettre à jour' : 'Ajouter'}</button>
        <a href="${pageContext.request.contextPath}/admin/trajets?action=list" class="btn btn-secondary">Annuler</a>
    </div>

</form>

<jsp:include page="/WEB-INF/jsp/admin/common/admin_footer.jsp" />

