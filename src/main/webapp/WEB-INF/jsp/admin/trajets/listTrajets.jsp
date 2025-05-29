<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<c:set var="pageTitle" value="Gestion des Trajets" scope="request"/>
<jsp:include page="/WEB-INF/jsp/admin/common/admin_header.jsp" />

<h2>Liste des Trajets</h2>

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
    <a href="${pageContext.request.contextPath}/admin/trajets?action=new" class="btn">Ajouter un Nouveau Trajet</a>
</div>

<div class="table-container">
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Gare de Départ</th>
                <th>Gare d'Arrivée</th>
                <th>Description</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${empty listTrajet}">
                    <tr>
                        <td colspan="5" class="text-center">Aucun trajet trouvé.</td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="trajet" items="${listTrajet}">
                        <tr>
                            <td>${trajet.id}</td>
                            <td><c:out value="${trajet.gareDepart.nom}"/> (<c:out value="${trajet.gareDepart.ville}"/>)</td>
                            <td><c:out value="${trajet.gareArrivee.nom}"/> (<c:out value="${trajet.gareArrivee.ville}"/>)</td>
                            <td><c:out value="${trajet.description}"/></td>
                            <td class="actions">
                                <a href="${pageContext.request.contextPath}/admin/trajets?action=edit&id=${trajet.id}" class="edit btn btn-sm btn-warning">Modifier</a>
                                &nbsp;
                                <a href="${pageContext.request.contextPath}/admin/trajets?action=delete&id=${trajet.id}" 
                                   class="delete btn btn-sm btn-danger" 
                                   onclick="return confirm('Êtes-vous sûr de vouloir supprimer ce trajet ? Cette action est irréversible.');">Supprimer</a>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>
</div>

<jsp:include page="/WEB-INF/jsp/admin/common/admin_footer.jsp" />

