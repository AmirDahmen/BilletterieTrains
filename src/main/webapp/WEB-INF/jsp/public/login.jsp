<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" />

<h2>Connexion</h2>

<c:if test="${not empty errorMessage}">
    <p class="error-message">${errorMessage}</p>
</c:if>
<c:if test="${not empty successMessage}">
    <p class="success-message">${successMessage}</p>
</c:if>

<form action="${pageContext.request.contextPath}/auth" method="post">
    <input type="hidden" name="action" value="login">
    <div class="form-group">
        <label for="email">Email :</label>
        <input type="email" id="email" name="email" required>
    </div>
    <div class="form-group">
        <label for="password">Mot de passe :</label>
        <input type="password" id="password" name="password" required>
    </div>
    <button type="submit" class="btn">Se Connecter</button>
</form>

<p class="mt-20">Pas encore de compte ? <a href="${pageContext.request.contextPath}/auth?action=showRegister">Inscrivez-vous ici</a></p>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp" />

