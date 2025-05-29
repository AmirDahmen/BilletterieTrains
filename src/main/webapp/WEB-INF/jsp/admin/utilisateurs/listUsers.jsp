<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<c:set var="pageTitle" value="Gestion des Utilisateurs" scope="request"/>
<jsp:include page="/WEB-INF/jsp/admin/common/admin_header.jsp" />

<h2>Liste des Utilisateurs</h2>

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
<c:if test="${not empty requestScope.infoMessage}">
    <p class="success-message" style="background-color: #fff3cd; color: #856404; border-color: #ffeeba;">${requestScope.infoMessage}</p> <%-- Info message styling --%>
</c:if>



<div class="table-container">
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Nom</th>
                <th>Prénom</th>
                <th>Email</th>
                <th>Rôle</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${empty listUser}">
                    <tr>
                        <td colspan="6" class="text-center">Aucun utilisateur trouvé.</td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="user" items="${listUser}">
                        <tr>
                            <td>${user.id}</td>
                            <td><c:out value="${user.lastName}"/></td>
                            <td><c:out value="${user.firstName}"/></td>
                            <td><c:out value="${user.email}"/></td>
                            <td>${user.role}</td>
                            <td class="actions">
                                <c:if test="${user.role == 'CLIENT'}">
                                    <c:choose>
                                        <c:when test="${user.banned}">
                                            <a href="${pageContext.request.contextPath}/admin/utilisateurs?action=unban&id=${user.id}" 
                                               class="btn btn-sm btn-success" 
                                               onclick="return confirm("Êtes-vous sûr de vouloir débannir cet utilisateur (${user.email}) ?");">Débannir</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="${pageContext.request.contextPath}/admin/utilisateurs?action=ban&id=${user.id}" 
                                               class="btn btn-sm btn-warning" 
                                               onclick="return confirm("Êtes-vous sûr de vouloir bannir cet utilisateur (${user.email}) ?");">Bannir</a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                                
                                <c:if test="${sessionScope.user.id != user.id}"> 
                                    <a href="${pageContext.request.contextPath}/admin/utilisateurs?action=delete&id=${user.id}" 
                                       class="delete btn btn-sm btn-danger" 
                                       style="margin-left: 5px;" 
                                       onclick="return confirm("Êtes-vous sûr de vouloir supprimer cet utilisateur (${user.email}) ? Cette action est irréversible et peut affecter les données associées (billets, etc.).");">Supprimer</a>
                                </c:if>
                                <c:if test="${sessionScope.user.id == user.id}">
                                    (Votre Compte)
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>
</div>

<jsp:include page="/WEB-INF/jsp/admin/common/admin_footer.jsp" />

