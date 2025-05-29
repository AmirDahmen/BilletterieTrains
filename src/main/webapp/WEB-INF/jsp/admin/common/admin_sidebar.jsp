<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<aside class="admin-sidebar">
    <ul>
        <c:set var="requestURI" value="${pageContext.request.requestURI}"/>

        <li><a href="${pageContext.request.contextPath}/admin/dashboard" 
               class="${requestURI.endsWith('/admin/dashboard') ? 'active' : ''}">Dashboard</a></li>
        <li><a href="${pageContext.request.contextPath}/admin/gares" 
               class="${requestURI.contains('/admin/gares') ? 'active' : ''}">Gestion Gares</a></li>
        <li><a href="${pageContext.request.contextPath}/admin/trajets" 
               class="${requestURI.contains('/admin/trajets') ? 'active' : ''}">Gestion Trajets</a></li>
        <li><a href="${pageContext.request.contextPath}/admin/voyages" 
               class="${requestURI.contains('/admin/voyages') ? 'active' : ''}">Gestion Voyages</a></li>
        <li><a href="${pageContext.request.contextPath}/admin/utilisateurs" 
               class="${requestURI.contains('/admin/utilisateurs') ? 'active' : ''}">Gestion Utilisateurs</a></li>
 <%-- 
    <li><a href="${pageContext.request.contextPath}/admin/paiements" 
           class="${requestURI.contains('/admin/paiements') ? 'active' : ''}">Gestion Paiements</a></li>
    <li><a href="${pageContext.request.contextPath}/admin/annulations" 
           class="${requestURI.contains('/admin/annulations') ? 'active' : ''}">Gestion Annulations</a></li>
--%>

    </ul>
</aside>

