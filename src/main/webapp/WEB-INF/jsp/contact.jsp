<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Contact - Safe Trip</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .contact-container {
            max-width: 800px;
            margin: 40px auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 0 15px rgba(0,0,0,0.1);
        }
        
        .contact-header {
            text-align: center;
            margin-bottom: 30px;
        }
        
        .contact-header h1 {
            color: #3498db;
            margin-bottom: 10px;
        }
        
        .contact-header p {
            color: #666;
            font-size: 16px;
        }
        
        .contact-form {
            max-width: 600px;
            margin: 0 auto;
        }
        
        .form-group {
            margin-bottom: 20px;
        }
        
        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
            color: #333;
        }
        
        .form-group input,
        .form-group textarea,
        .form-group select {
            width: 100%;
            padding: 12px;
            border: 2px solid #ddd;
            border-radius: 6px;
            font-size: 16px;
            transition: border-color 0.3s;
            box-sizing: border-box;
        }
        
        .form-group input:focus,
        .form-group textarea:focus,
        .form-group select:focus {
            outline: none;
            border-color: #3498db;
        }
        
        .form-group textarea {
            height: 120px;
            resize: vertical;
        }
        
        .btn-submit {
            background-color: #3498db;
            color: white;
            border: none;
            padding: 15px 30px;
            border-radius: 6px;
            cursor: pointer;
            font-size: 16px;
            font-weight: bold;
            width: 100%;
            transition: background-color 0.3s;
        }
        
        .btn-submit:hover {
            background-color: #2980b9;
        }
        
        .success-message {
            background-color: #d4edda;
            color: #155724;
            padding: 15px;
            border-radius: 6px;
            margin-bottom: 20px;
            text-align: center;
            border: 1px solid #c3e6cb;
        }
        
        .contact-info {
            background-color: #f8f9fa;
            padding: 20px;
            border-radius: 6px;
            margin-top: 30px;
            text-align: center;
        }
        
        .contact-info h3 {
            color: #3498db;
            margin-bottom: 15px;
        }
        
        .contact-info p {
            margin: 5px 0;
            color: #666;
        }
        
        .user-info {
            background-color: #e8f4fd;
            padding: 10px;
            border-radius: 4px;
            margin-bottom: 15px;
            font-size: 14px;
            color: #2c5aa0;
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/common/header.jsp" />
    
    <div class="contact-container">
        <div class="contact-header">
            <h1>Contactez-nous</h1>
            <p>Nous sommes là pour vous aider ! N'hésitez pas à nous contacter pour toute question ou suggestion.</p>
        </div>
        
        <c:if test="${not empty successMessage}">
            <div class="success-message">
                ${successMessage}
            </div>
        </c:if>
        
        <c:if test="${not empty sessionScope.user}">
            <div class="user-info">
                <strong>Connecté en tant que :</strong> ${sessionScope.user.firstName} ${sessionScope.user.lastName} (${sessionScope.user.email})
            </div>
        </c:if>
        
        <form action="https://formspree.io/f/manordkg" method="POST" class="contact-form">
            <div class="form-group">
                <label for="name">Nom complet *</label>
                <input type="text" id="name" name="name" 
                       value="<c:if test='${not empty sessionScope.user}'>${sessionScope.user.firstName} ${sessionScope.user.lastName}</c:if>" 
                       required>
            </div>
            
            <div class="form-group">
                <label for="email">Email *</label>
                <input type="email" id="email" name="email" 
                       value="<c:if test='${not empty sessionScope.user}'>${sessionScope.user.email}</c:if>" 
                       required>
            </div>
            
            <div class="form-group">
                <label for="subject">Sujet *</label>
                <select id="subject" name="subject" required>
                    <option value="">Sélectionnez un sujet</option>
                    <option value="Question générale">Question générale</option>
                    <option value="Problème technique">Problème technique</option>
                    <option value="Réservation">Question sur une réservation</option>
                    <option value="Remboursement">Demande de remboursement</option>
                    <option value="Suggestion">Suggestion d'amélioration</option>
                    <option value="Autre">Autre</option>
                </select>
            </div>
            
            <div class="form-group">
                <label for="message">Message *</label>
                <textarea id="message" name="message" placeholder="Décrivez votre demande en détail..." required></textarea>
            </div>
            
            <input type="hidden" name="_subject" value="Nouveau message depuis Safe Trip">
            <input type="hidden" name="_next" value="${pageContext.request.contextPath}/contact?success=true">
            <input type="hidden" name="_captcha" value="false">
            
            <c:if test="${not empty sessionScope.user}">
                <input type="hidden" name="user_id" value="${sessionScope.user.id}">
                <input type="hidden" name="user_role" value="${sessionScope.user.role}">
            </c:if>
            
            <button type="submit" class="btn-submit">Envoyer le message</button>
        </form>
        
        <div class="contact-info">
            <h3>Autres moyens de nous contacter</h3>
            <p><strong>Email :</strong> support@safetrip.com</p>
            <p><strong>Téléphone :</strong> +216 54417837</p>
            <p><strong>Horaires :</strong> Lundi - Vendredi, 9h00 - 18h00</p>
        </div>
    </div>
    
<jsp:include page="/WEB-INF/jsp/common/footer.jsp" />
</body>
</html>