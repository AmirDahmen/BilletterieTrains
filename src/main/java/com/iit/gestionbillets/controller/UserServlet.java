package com.iit.gestionbillets.controller;

import com.iit.gestionbillets.dao.Interface.IUserDAO;
import com.iit.gestionbillets.dao.impl.UserDAOImpl;
import com.iit.gestionbillets.model.Role;
import com.iit.gestionbillets.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private IUserDAO userDAO;

    @Override
    public void init() throws ServletException {
        this.userDAO = new UserDAOImpl(); 
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession(false) == null || request.getSession(false).getAttribute("userRole") != com.iit.gestionbillets.model.Role.ADMIN) {
             response.sendRedirect(request.getContextPath() + "/auth?action=showLogin");
             return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "list"; 
        }

        try {
            switch (action) {
                case "ban":
                    toggleUserBanStatus(request, response, true);
                    break;
                case "unban":
                    toggleUserBanStatus(request, response, false);
                    break;
                
                 case "delete": 
                     deleteUser(request, response);
                     break;
                case "list":
                default:
                    listUsers(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace(); 
            request.setAttribute("errorMessage", "Une erreur est survenue lors de la gestion des utilisateurs.");
            request.getRequestDispatcher("/WEB-INF/jsp/admin/common/admin_error.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession(false) == null || request.getSession(false).getAttribute("userRole") != com.iit.gestionbillets.model.Role.ADMIN) {
             response.sendRedirect(request.getContextPath() + "/auth?action=showLogin");
             return;
        }
        
   
         listUsers(request, response);
    }

    private void listUsers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> listUser = userDAO.findAll(); 
        request.setAttribute("listUser", listUser);
        
        request.getRequestDispatcher("/WEB-INF/jsp/admin/utilisateurs/listUsers.jsp").forward(request, response);
    }
    
    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            User loggedInUser = (User) request.getSession().getAttribute("user");
            if (loggedInUser != null && loggedInUser.getId().equals(id)) {
                 request.getSession().setAttribute("errorMessage", "Vous ne pouvez pas supprimer votre propre compte administrateur.");
            } else {
                
                 request.getSession().setAttribute("errorMessage", "La suppression d\\'utilisateur n\\'est pas encore implémentée dans le DAO ou est désactivée pour des raisons de sécurité.");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID utilisateur invalide pour la suppression.");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Erreur lors de la tentative de suppression de l\\'utilisateur. Vérifiez les dépendances (billets, etc.).");
        }
        response.sendRedirect(request.getContextPath() + "/admin/utilisateurs?action=list");
    }
    
    

private void toggleUserBanStatus(HttpServletRequest request, HttpServletResponse response, boolean ban) throws IOException {
    try {
        Long id = Long.parseLong(request.getParameter("id"));
        User loggedInUser = (User) request.getSession().getAttribute("user");

        if (loggedInUser != null && loggedInUser.getId().equals(id)) {
            request.getSession().setAttribute("errorMessage", "Vous ne pouvez pas modifier le statut de votre propre compte.");
            response.sendRedirect(request.getContextPath() + "/admin/utilisateurs?action=list");
            return;
        }

        Optional<User> userOpt = userDAO.findById(id);
        if (userOpt.isPresent()) {
            User userToModify = userOpt.get();
            // Only allow banning CLIENTs
            if (userToModify.getRole() == Role.CLIENT) {
                userToModify.setBanned(ban);
                userDAO.update(userToModify);
                request.getSession().setAttribute("successMessage", "Statut de l'utilisateur " + userToModify.getEmail() + " mis à jour avec succès.");
            } else {
                request.getSession().setAttribute("errorMessage", "Vous ne pouvez bannir ou débannir que les utilisateurs avec le rôle CLIENT.");
            }
        } else {
            request.getSession().setAttribute("errorMessage", "Utilisateur non trouvé.");
        }
    } catch (NumberFormatException e) {
        request.getSession().setAttribute("errorMessage", "ID utilisateur invalide.");
    } catch (Exception e) {
        e.printStackTrace();
        request.getSession().setAttribute("errorMessage", "Erreur lors de la modification du statut de l'utilisateur.");
    }
    response.sendRedirect(request.getContextPath() + "/admin/utilisateurs?action=list");
}
}
