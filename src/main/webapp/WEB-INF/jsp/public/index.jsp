<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<c:set var="pageTitle" value="Accueil - Réservez vos Billets" scope="request"/>
<jsp:include page="/WEB-INF/jsp/common/header.jsp" />

<div class="hero-section">
    <h1>Bienvenue  !</h1>
    <p>Recherchez et réservez vos billets de train facilement.</p>
</div>

<div class="search-form-container">
    <h2>Rechercher un Voyage</h2>
    <form action="${pageContext.request.contextPath}/recherche" method="get">
        <input type="hidden" name="action" value="search">
        
        <div class="form-row">
            <div class="form-group">
                <label for="gareDepartId">Gare de Départ :</label>
                <select id="gareDepartId" name="gareDepartId" class="form-control" required>
                    <option value="">-- Sélectionner --</option>
                    <c:forEach var="gare" items="${listGare}">
                        <option value="${gare.id}">
                            <c:out value="${gare.nom}"/> (<c:out value="${gare.ville}"/>)
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="gareArriveeId">Gare d'Arrivée :</label>
                <select id="gareArriveeId" name="gareArriveeId" class="form-control" required>
                    <option value="">-- Sélectionner --</option>
                    <c:forEach var="gare" items="${listGare}">
                        <option value="${gare.id}">
                            <c:out value="${gare.nom}"/> (<c:out value="${gare.ville}"/>)
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="dateVoyage">Date de Voyage :</label>
                <jsp:useBean id="today" class="java.util.Date" />
                <input type="date" id="dateVoyage" name="dateVoyage" class="form-control" required 
                       min="<fmt:formatDate value="${today}" pattern="yyyy-MM-dd"/>">
            </div>
            
            <div class="form-group align-self-end">
                <button type="submit" class="btn btn-primary">🔍 Rechercher</button>
            </div>
        </div>
    </form>
</div>

<div class="info-section">
    <h3>Voyagez en toute sérénité</h3>
    <p>Découvrez nos offres et planifiez votre prochain voyage en toute tranquillité.</p>
</div>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp" />
