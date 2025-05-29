<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<c:set var="isEdit" value="${not empty gare.id}"/>
<c:set var="pageTitle" value="${isEdit ? 'Modifier la Gare' : 'Ajouter une Gare'}" scope="request"/>
<jsp:include page="/WEB-INF/jsp/admin/common/admin_header.jsp" />

<h2>${isEdit ? 'Modifier la Gare' : 'Ajouter une Nouvelle Gare'}</h2>

<c:if test="${not empty errorMessage}">
    <p class="error-message">${errorMessage}</p>
</c:if>

<form action="${pageContext.request.contextPath}/admin/gares" method="post">

    <input type="hidden" name="action" value="${formAction}">

    <c:if test="${isEdit}">
        <input type="hidden" name="id" value="${gare.id}">
    </c:if>

    <div class="form-group">
        <label for="nom">Nom de la Gare :</label>
        <input type="text" id="nom" name="nom" class="form-control" 
               value="<c:out value='${gare.nom}'/>" required>
    </div>

    <div class="form-group">
        <label for="ville">Ville :</label>
        <input type="text" id="ville" name="ville" class="form-control" 
               value="<c:out value='${gare.ville}'/>" required>
    </div>

    <div class="form-group">
        <button type="submit" class="btn">${isEdit ? 'Mettre à jour' : 'Ajouter'}</button>
        <a href="${pageContext.request.contextPath}/admin/gares?action=list" class="btn btn-secondary">Annuler</a>
    </div>

</form>

<jsp:include page="/WEB-INF/jsp/admin/common/admin_footer.jsp" />

