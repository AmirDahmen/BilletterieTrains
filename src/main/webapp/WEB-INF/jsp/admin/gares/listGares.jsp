<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %> 

<c:set var="pageTitle" value="Gestion des Gares" scope="request"/>
<jsp:include page="/WEB-INF/jsp/admin/common/admin_header.jsp" />

<h2>Liste des Gares</h2>

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
    <a href="${pageContext.request.contextPath}/admin/gares?action=new" class="btn">Ajouter une Nouvelle Gare</a>
</div>

<div class="table-container">
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Nom de la Gare</th>
                <th>Ville</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${empty listGare}">
                    <tr>
                        <td colspan="4" class="text-center">Aucune gare trouvée.</td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="gare" items="${listGare}">
                        <tr>
                            <td>${gare.id}</td>
                            <td><c:out value="${gare.nom}"/></td>
                            <td><c:out value="${gare.ville}"/></td>
                            <td class="actions">
                                <a href="${pageContext.request.contextPath}/admin/gares?action=edit&id=${gare.id}" class="edit btn btn-sm btn-warning">Modifier</a>
                                &nbsp;
                                <a href="${pageContext.request.contextPath}/admin/gares?action=delete&id=${gare.id}" 
                                   class="delete btn btn-sm btn-danger" 
                                   onclick="return confirm(\'Êtes-vous sûr de vouloir supprimer cette gare ? Cette action est irréversible.\');">Supprimer</a>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>
</div>

<jsp:include page="/WEB-INF/jsp/admin/common/admin_footer.jsp" />

