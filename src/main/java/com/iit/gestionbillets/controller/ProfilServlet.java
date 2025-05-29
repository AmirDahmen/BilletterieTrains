package com.iit.gestionbillets.controller;

import com.iit.gestionbillets.dao.Interface.IUserDAO;
import com.iit.gestionbillets.dao.impl.UserDAOImpl;
import com.iit.gestionbillets.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/profil")
public class ProfilServlet extends HttpServlet {

    private IUserDAO userDAO = new UserDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        request.setAttribute("user", user);
        
        request.getRequestDispatcher("/WEB-INF/jsp/user/profil.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        
        // Récupérer  données 
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String gender = request.getParameter("gender");
        
        // Mettre à jour les informations 
        user.setFirstName(firstName);
        user.setLastName(lastName);
        
        if ("FEMME".equals(gender)) {
            user.setGender(User.Gender.FEMME);
        } else {
            user.setGender(User.Gender.HOMME);
        }
        
        // Sauvegarder 
        userDAO.update(user);
        
        session.setAttribute("user", user);
        
        request.setAttribute("successMessage", "Profil mis à jour avec succès !");
        request.setAttribute("user", user);
        
        request.getRequestDispatcher("/WEB-INF/jsp/user/profil.jsp").forward(request, response);
    }
}