package com.iit.gestionbillets.controller;

import com.iit.gestionbillets.dao.Interface.IGareDAO;
import com.iit.gestionbillets.dao.impl.GareDAOImpl;
import com.iit.gestionbillets.model.Gare;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


public class GareServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private IGareDAO gareDAO;

    @Override
    public void init() throws ServletException {
        // Instantiation de  DAO 
        this.gareDAO = new GareDAOImpl();
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
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteGare(request, response);
                    break;
                case "list":
                default:
                    listGares(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace(); 
            request.setAttribute("errorMessage", "Une erreur est survenue lors du traitement de votre demande.");
            request.getRequestDispatcher("/WEB-INF/jsp/admin/common/admin_error.jsp").forward(request, response); 
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession(false) == null || request.getSession(false).getAttribute("userRole") != com.iit.gestionbillets.model.Role.ADMIN) {
             response.sendRedirect(request.getContextPath() + "/auth?action=showLogin");
             return;
        }
        
        String action = request.getParameter("action");
         if ("insert".equals(action)) {
            insertGare(request, response);
        } else if ("update".equals(action)) {
            updateGare(request, response);
        } else {
             listGares(request, response); 
        }
    }

    private void listGares(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Gare> listGare = gareDAO.findAll();
        request.setAttribute("listGare", listGare);
        request.getRequestDispatcher("/WEB-INF/jsp/admin/gares/listGares.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("gare", new Gare()); 
        request.setAttribute("formAction", "insert");
        request.getRequestDispatcher("/WEB-INF/jsp/admin/gares/formGare.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Optional<Gare> existingGare = gareDAO.findById(id);
            if (existingGare.isPresent()) {
                request.setAttribute("gare", existingGare.get());
                request.setAttribute("formAction", "update");
                request.getRequestDispatcher("/WEB-INF/jsp/admin/gares/formGare.jsp").forward(request, response);
            } else {
                 request.setAttribute("errorMessage", "Gare non trouvée pour modification.");
                 listGares(request, response);
            }
        } catch (NumberFormatException e) {
             request.setAttribute("errorMessage", "ID de gare invalide.");
             listGares(request, response);
        }
    }

    private void insertGare(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String nom = request.getParameter("nom");
        String ville = request.getParameter("ville");

        if (nom == null || nom.trim().isEmpty() || ville == null || ville.trim().isEmpty()) {
             request.setAttribute("errorMessage", "Le nom et la ville de la gare sont obligatoires.");
             request.setAttribute("gare", new Gare(nom, ville)); 
             request.setAttribute("formAction", "insert");
             request.getRequestDispatcher("/WEB-INF/jsp/admin/gares/formGare.jsp").forward(request, response);
             return;
        }
        
        // Check if name already exists
        if (gareDAO.findByNom(nom.trim()).isPresent()) {
             request.setAttribute("errorMessage", "Une gare avec ce nom existe déjà.");
             request.setAttribute("gare", new Gare(nom, ville)); 
             request.setAttribute("formAction", "insert");
             request.getRequestDispatcher("/WEB-INF/jsp/admin/gares/formGare.jsp").forward(request, response);
             return;
        }

        Gare newGare = new Gare(nom.trim(), ville.trim());
        gareDAO.save(newGare);
        request.getSession().setAttribute("successMessage", "Gare ajoutée avec succès !"); 
        response.sendRedirect(request.getContextPath() + "/admin/gares?action=list");
    }

    private void updateGare(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String idParam = request.getParameter("id");
        String nom = request.getParameter("nom");
        String ville = request.getParameter("ville");
        Long id = null;

         try {
             id = Long.parseLong(idParam);
         } catch (NumberFormatException e) {
             request.getSession().setAttribute("errorMessage", "ID de gare invalide pour la mise à jour.");
             response.sendRedirect(request.getContextPath() + "/admin/gares?action=list");
             return;
         }
         
        if (nom == null || nom.trim().isEmpty() || ville == null || ville.trim().isEmpty()) {
             request.setAttribute("errorMessage", "Le nom et la ville de la gare sont obligatoires.");
             Optional<Gare> existingGare = gareDAO.findById(id);
             if(existingGare.isPresent()) {
                 Gare gareToEdit = existingGare.get();
                 gareToEdit.setNom(nom); 
                 gareToEdit.setVille(ville);
                 request.setAttribute("gare", gareToEdit);
                 request.setAttribute("formAction", "update");
                 request.getRequestDispatcher("/WEB-INF/jsp/admin/gares/formGare.jsp").forward(request, response);
             } else {
                 request.getSession().setAttribute("errorMessage", "Gare non trouvée pour la mise à jour.");
                 response.sendRedirect(request.getContextPath() + "/admin/gares?action=list");
             }
             return;
        }
        
        Optional<Gare> existingByName = gareDAO.findByNom(nom.trim());
        if (existingByName.isPresent() && !existingByName.get().getId().equals(id)) {
             request.setAttribute("errorMessage", "Une autre gare avec ce nom existe déjà.");
             Optional<Gare> currentGareOpt = gareDAO.findById(id);
             if(currentGareOpt.isPresent()){
                 request.setAttribute("gare", currentGareOpt.get()); 
                 request.setAttribute("formAction", "update");
                 request.getRequestDispatcher("/WEB-INF/jsp/admin/gares/formGare.jsp").forward(request, response);
             } else {
                 request.getSession().setAttribute("errorMessage", "Gare non trouvée pour la mise à jour.");
                 response.sendRedirect(request.getContextPath() + "/admin/gares?action=list");
             }
             return;
        }

        Gare gare = new Gare(nom.trim(), ville.trim());
        gare.setId(id);
        gareDAO.update(gare);
        request.getSession().setAttribute("successMessage", "Gare mise à jour avec succès !"); 
        response.sendRedirect(request.getContextPath() + "/admin/gares?action=list");
    }

    private void deleteGare(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            gareDAO.delete(id);
            request.getSession().setAttribute("successMessage", "Gare supprimée avec succès !"); // Use session for flash message
        } catch (NumberFormatException e) {
             request.getSession().setAttribute("errorMessage", "ID de gare invalide pour la suppression.");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Erreur lors de la suppression de la gare. Elle est peut-être utilisée ailleurs.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/gares?action=list");
    }
}

