<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<c:set var="pageTitle" value="Détails du Billet" scope="request"/>
<jsp:include page="/WEB-INF/jsp/common/header.jsp" />

<div class="container">
    <h2>🚂 Billet de Train</h2>
    
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">
            <p>${errorMessage}</p>
        </div>
    </c:if>
    
    <c:if test="${not empty billet}">
        <div class="ticket-horizontal">
            <div class="ticket-header-horizontal">
                <div class="company-info">
                    <h3>🚂 Safe Trip</h3>
                    <small>Billet Électronique</small>
                </div>
                <div class="ticket-ref">
                    <strong><c:out value="${billet.referenceBillet}"/></strong>
                </div>
            </div>
            
            <div class="ticket-body">
                <div class="journey-section">
                    <div class="route-horizontal">
                        <div class="station departure-station">
                            <div class="station-name"><c:out value="${billet.voyage.trajet.gareDepart.nom}"/></div>
                            <div class="station-city"><c:out value="${billet.voyage.trajet.gareDepart.ville}"/></div>
                            <div class="time-info">
                                <div class="date">${billet.voyage.heureDepart.format(DateTimeFormatter.ofPattern('dd/MM/yyyy'))}</div>
                                <div class="time">${billet.voyage.heureDepart.format(DateTimeFormatter.ofPattern('HH:mm'))}</div>
                            </div>
                        </div>
                        
                        <div class="journey-arrow">
                            <div class="arrow-line"></div>
                            <div class="train-icon">🚂</div>
                        </div>
                        
                        <div class="station arrival-station">
                            <div class="station-name"><c:out value="${billet.voyage.trajet.gareArrivee.nom}"/></div>
                            <div class="station-city"><c:out value="${billet.voyage.trajet.gareArrivee.ville}"/></div>
                            <div class="time-info">
                                <div class="date">${billet.voyage.heureArrivee.format(DateTimeFormatter.ofPattern('dd/MM/yyyy'))}</div>
                                <div class="time">${billet.voyage.heureArrivee.format(DateTimeFormatter.ofPattern('HH:mm'))}</div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="passenger-price-row">
                        <div class="passenger-info-compact">
                            <span class="label">👤 Passager:</span>
                            <span class="value"><c:out value="${billet.user.firstName}"/> <c:out value="${billet.user.lastName}"/></span>
                        </div>
                        <div class="price-info-compact">
                            <span class="label">💰 Prix:</span>
                            <span class="price-value"><fmt:formatNumber value="${billet.prixPaye}" type="currency" currencySymbol="€"/></span>
                        </div>
                    </div>
                </div>
                
                <div class="qr-section">
                    <c:choose>
                        <c:when test="${not empty qrCodeBase64}">
                            <div class="qr-code-compact">
                                <img src="data:image/png;base64,${qrCodeBase64}" alt="QR Code" class="qr-image">
                                <small>Scan pour valider</small>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="qr-placeholder-compact">
                                <div class="qr-placeholder-box">QR</div>
                                <small>En cours...</small>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            
            <div class="ticket-footer">
                <div class="booking-details">
                    <small>📅 Réservé le: ${billet.dateReservation.format(DateTimeFormatter.ofPattern('dd/MM/yyyy HH:mm'))}</small>
                    <small>📧 ${billet.user.email}</small>
                </div>
                <div class="ticket-actions-compact">
                    <a href="${pageContext.request.contextPath}/billet?action=downloadPdf&ref=${billet.referenceBillet}" 
                       class="btn-compact btn-download">📄 PDF</a>
                    <a href="${pageContext.request.contextPath}/client/dashboard" 
                       class="btn-compact btn-back">🔙 Retour</a>
                </div>
            </div>
        </div>
    </c:if>
    
    <c:if test="${empty billet}">
        <div class="alert alert-warning">
            <p>Aucun billet à afficher.</p>
        </div>
        <div class="actions">
            <a href="${pageContext.request.contextPath}/client/dashboard" class="btn btn-primary">🔙 Retour au Dashboard</a>
        </div>
    </c:if>
</div>

<style>
.ticket-horizontal {
    max-width: 900px;
    margin: 20px auto;
    background: white;
    border: 2px solid #007bff;
    border-radius: 15px;
    box-shadow: 0 6px 20px rgba(0,123,255,0.15);
    overflow: hidden;
    font-family: 'Arial', sans-serif;
}

.ticket-header-horizontal {
    background: linear-gradient(135deg, #007bff, #0056b3);
    color: white;
    padding: 15px 25px;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.company-info h3 {
    margin: 0;
    font-size: 20px;
}

.company-info small {
    font-size: 12px;
    opacity: 0.9;
}

.ticket-ref {
    background: rgba(255,255,255,0.2);
    padding: 8px 15px;
    border-radius: 20px;
    font-size: 14px;
    font-weight: bold;
}

.ticket-body {
    padding: 25px;
    display: flex;
    gap: 25px;
    align-items: center;
}

.journey-section {
    flex: 1;
}

.route-horizontal {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 20px;
    padding: 20px;
    background: #f8f9fa;
    border-radius: 12px;
    border-left: 4px solid #007bff;
}

.station {
    text-align: center;
    flex: 1;
}

.station-name {
    font-weight: bold;
    font-size: 16px;
    color: #333;
    margin-bottom: 5px;
}

.station-city {
    font-size: 12px;
    color: #666;
    margin-bottom: 8px;
}

.time-info .date {
    font-size: 12px;
    color: #666;
}

.time-info .time {
    font-size: 18px;
    font-weight: bold;
    color: #007bff;
}

.journey-arrow {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin: 0 20px;
    position: relative;
}

.arrow-line {
    width: 60px;
    height: 2px;
    background: #007bff;
    position: relative;
}

.arrow-line::after {
    content: '';
    position: absolute;
    right: -5px;
    top: -3px;
    width: 0;
    height: 0;
    border-left: 8px solid #007bff;
    border-top: 4px solid transparent;
    border-bottom: 4px solid transparent;
}

.train-icon {
    font-size: 16px;
    margin-top: 5px;
}

.passenger-price-row {
    display: flex;
    justify-content: space-around;
    align-items: center;
    gap: 20px;
    padding: 15px 0;
    border-top: 1px solid #eee;
}

.passenger-info-compact, .price-info-compact {
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
}

.label {
    font-size: 11px;
    color: #666;
    margin-bottom: 3px;
}

.value, .price-value {
    font-weight: bold;
    font-size: 14px;
    color: #333;
}

.price-value {
    color: #28a745;
    font-size: 16px;
}

.qr-section {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20px;
    background: #f8f9fa;
    border-radius: 12px;
    border: 1px solid #dee2e6;
}

.qr-code-compact {
    text-align: center;
}

.qr-image {
    width: 120px;
    height: 120px;
    border: 1px solid #ddd;
    border-radius: 8px;
    margin-bottom: 8px;
}

.qr-placeholder-compact {
    text-align: center;
}

.qr-placeholder-box {
    width: 120px;
    height: 120px;
    border: 2px dashed #ccc;
    border-radius: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 24px;
    color: #999;
    margin-bottom: 8px;
}

.qr-section small {
    font-size: 10px;
    color: #666;
}

.ticket-footer {
    background: #f8f9fa;
    padding: 15px 25px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-top: 1px solid #dee2e6;
}

.booking-details {
    display: flex;
    flex-direction: column;
    gap: 3px;
}

.booking-details small {
    font-size: 11px;
    color: #666;
}

.ticket-actions-compact {
    display: flex;
    gap: 10px;
}

.btn-compact {
    padding: 8px 15px;
    text-decoration: none;
    border-radius: 20px;
    font-size: 12px;
    font-weight: bold;
    transition: all 0.3s;
    border: none;
    cursor: pointer;
}

.btn-download {
    background: #6c757d;
    color: white;
}

.btn-back {
    background: #007bff;
    color: white;
}

.btn-compact:hover {
    transform: translateY(-1px);
    box-shadow: 0 2px 8px rgba(0,0,0,0.2);
}

.alert {
    padding: 15px;
    margin: 20px 0;
    border-radius: 5px;
}

.alert-danger {
    background: #f8d7da;
    color: #721c24;
    border: 1px solid #f5c6cb;
}

.alert-warning {
    background: #fff3cd;
    color: #856404;
    border: 1px solid #ffeaa7;
}

/* Responsive */
@media (max-width: 768px) {
    .ticket-body {
        flex-direction: column;
        gap: 20px;
    }
    
    .route-horizontal {
        flex-direction: column;
        gap: 15px;
    }
    
    .journey-arrow {
        transform: rotate(90deg);
    }
    
    .passenger-price-row {
        flex-direction: column;
        gap: 10px;
    }
    
    .ticket-footer {
        flex-direction: column;
        gap: 15px;
        text-align: center;
    }
}
</style>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp" />
