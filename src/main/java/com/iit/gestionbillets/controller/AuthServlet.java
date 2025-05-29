package com.iit.gestionbillets.controller;

import com.iit.gestionbillets.model.Role;
import com.iit.gestionbillets.model.User;
import com.iit.gestionbillets.service.Interface.IAuthService;
import com.iit.gestionbillets.service.impl.AuthService; // Assuming direct instantiation

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

public class AuthServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private IAuthService authService;

    @Override
    public void init() throws ServletException {
        this.authService = new AuthService();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("login".equals(action)) {
            handleLogin(request, response);
        } else if ("register".equals(action)) {
            handleRegister(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/"); 
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("logout".equals(action)) {
            handleLogout(request, response);
        } else if ("showLogin".equals(action)) {
            request.getRequestDispatcher("/WEB-INF/jsp/public/login.jsp").forward(request, response);
        } else if ("showRegister".equals(action)) {
            request.getRequestDispatcher("/WEB-INF/jsp/public/inscription.jsp").forward(request, response);
        } else {
            
             response.sendRedirect(request.getContextPath() + "/");
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("errorMessage", "Email et mot de passe requis.");
            request.getRequestDispatcher("/WEB-INF/jsp/public/login.jsp").forward(request, response);
            return;
        }

        Optional<User> userOptional = authService.login(email, password);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            
            // Vérification si l'utilisateur est banni
            if (user.isBanned()) {
                System.out.println("Login attempt by banned user: " + user.getEmail()); // Debugging
                request.setAttribute("errorMessage", "Votre compte a été suspendu. Veuillez contacter l'administrateur.");
                request.getRequestDispatcher("/WEB-INF/jsp/public/login.jsp").forward(request, response);
                return;
            }
            
            HttpSession session = request.getSession(true); 
            session.setAttribute("user", user); 
            session.setAttribute("userRole", user.getRole());
            session.setAttribute("userName", user.getFirstName()); 
            System.out.println("Login successful for: " + user.getEmail() + " Role: " + user.getRole()); 

            // Redirect based on role
            if (user.getRole() == Role.ADMIN) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else if (user.getRole() == Role.CLIENT) {
                response.sendRedirect(request.getContextPath() + "/client/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/");
            }
        } else {
            System.out.println("Login failed for: " + email); 
            request.setAttribute("errorMessage", "Email ou mot de passe incorrect.");
            request.getRequestDispatcher("/WEB-INF/jsp/public/login.jsp").forward(request, response);
        }
    }


    private void handleRegister(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");

        if (email == null || email.isEmpty() || password == null || password.isEmpty() || 
            firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty()) {
            request.setAttribute("errorMessage", "Tous les champs sont requis.");
            request.getRequestDispatcher("/WEB-INF/jsp/public/inscription.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Les mots de passe ne correspondent pas.");
            request.getRequestDispatcher("/WEB-INF/jsp/public/inscription.jsp").forward(request, response);
            return;
        }

        boolean success = authService.register(email, password, firstName, lastName);

        if (success) {
            request.setAttribute("successMessage", "Inscription réussie ! Vous pouvez maintenant vous connecter.");
            request.getRequestDispatcher("/WEB-INF/jsp/public/login.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "L\'email existe déjà ou une erreur s\'est produite.");
            request.getRequestDispatcher("/WEB-INF/jsp/public/inscription.jsp").forward(request, response);
        }
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false); 
        if (session != null) {
            System.out.println("Logging out user: " + session.getAttribute("userName")); 
            session.invalidate(); 
        }
        response.sendRedirect(request.getContextPath() + "/");
    }
}

