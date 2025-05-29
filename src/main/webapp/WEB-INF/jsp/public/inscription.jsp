<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" />

<h2>Inscription</h2>

<c:if test="${not empty errorMessage}">
    <p class="error-message">${errorMessage}</p>
</c:if>

<form action="${pageContext.request.contextPath}/auth" method="post">
    <input type="hidden" name="action" value="register">
    <div class="form-group">
        <label for="firstName">Prénom :</label>
        <input type="text" id="firstName" name="firstName" required value="<c:out value='${param.firstName}'/>">
    </div>
    <div class="form-group">
        <label for="lastName">Nom :</label>
        <input type="text" id="lastName" name="lastName" required value="<c:out value='${param.lastName}'/>">
    </div>
    <div class="form-group">
        <label for="email">Email :</label>
        <input type="email" id="email" name="email" required value="<c:out value='${param.email}'/>">
    </div>
    <div class="form-group">
        <label for="password">Mot de passe :</label>
        <input type="password" id="password" name="password" required>
    </div>
    <div class="form-group">
        <label for="confirmPassword">Confirmer le mot de passe :</label>
        <input type="password" id="confirmPassword" name="confirmPassword" required>
    </div>
    <button type="submit" class="btn">S'inscrire</button>
</form>

<p class="mt-20">Déjà un compte ? <a href="${pageContext.request.contextPath}/auth?action=showLogin">Connectez-vous ici</a></p>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp" />

