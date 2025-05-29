<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mon Profil - Safe Trip</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .profile-container {
            max-width: 800px;
            margin: 20px auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        
        .profile-header {
            display: flex;
            align-items: center;
            margin-bottom: 30px;
        }
        
        .profile-image {
            width: 120px;
            height: 120px;
            border-radius: 50%;
            object-fit: cover;
            margin-right: 20px;
            border: 3px solid #3498db;
        }
        
        .profile-info h1 {
            margin: 0 0 10px 0;
            color: #333;
        }
        
        .profile-info p {
            margin: 0;
            color: #666;
        }
        
        .profile-form {
            margin-top: 20px;
        }
        
        .form-group {
            margin-bottom: 15px;
        }
        
        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        
        .form-group input, .form-group select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        
        .btn-update {
            background-color: #3498db;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }
        
        .btn-update:hover {
            background-color: #2980b9;
        }
        
        .success-message {
            background-color: #d4edda;
            color: #155724;
            padding: 10px;
            border-radius: 4px;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/common/header.jsp" />
    
    <div class="profile-container">
        <c:if test="${not empty successMessage}">
            <div class="success-message">
                ${successMessage}
            </div>
        </c:if>
        
        <div class="profile-header">
            <img src="${pageContext.request.contextPath}${user.profileImagePath}" alt="Photo de profil" class="profile-image">
            <div class="profile-info">
                <h1>${user.firstName} ${user.lastName}</h1>
                <p>${user.email}</p>
                <p>Genre: ${user.gender.displayName}</p>
            </div>
        </div>
        
        <h2>Modifier mon profil</h2>
        <form action="${pageContext.request.contextPath}/profil" method="post" class="profile-form">
            <div class="form-group">
                <label for="firstName">Prénom</label>
                <input type="text" id="firstName" name="firstName" value="${user.firstName}" required>
            </div>
            
            <div class="form-group">
                <label for="lastName">Nom</label>
                <input type="text" id="lastName" name="lastName" value="${user.lastName}" required>
            </div>
            
            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" value="${user.email}" disabled>
                <small>L'email ne peut pas être modifié</small>
            </div>
            
            <div class="form-group">
                <label for="gender">Genre</label>
                <select id="gender" name="gender">
                    <option value="HOMME" ${user.gender == 'HOMME' ? 'selected' : ''}>Homme</option>
                    <option value="FEMME" ${user.gender == 'FEMME' ? 'selected' : ''}>Femme</option>
                </select>
            </div>
            
            <button type="submit" class="btn-update">Mettre à jour</button>
        </form>
    </div>
    
<jsp:include page="/WEB-INF/jsp/common/footer.jsp" />
</body>
</html>