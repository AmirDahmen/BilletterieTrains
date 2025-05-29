package com.iit.gestionbillets.controller;

import com.iit.gestionbillets.dao.Interface.IGareDAO;
import com.iit.gestionbillets.dao.Interface.ITrajetDAO;
import com.iit.gestionbillets.dao.impl.GareDAOImpl;
import com.iit.gestionbillets.dao.impl.TrajetDAOImpl;
import com.iit.gestionbillets.model.Gare;
import com.iit.gestionbillets.model.Trajet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


public class TrajetServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ITrajetDAO trajetDAO;
    private IGareDAO gareDAO; 

    @Override
    public void init() throws ServletException {
        this.trajetDAO = new TrajetDAOImpl();
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
                    showNewEditForm(request, response, false);
                    break;
                case "edit":
                    showNewEditForm(request, response, true);
                    break;
                case "delete":
                    deleteTrajet(request, response);
                    break;
                case "list":
                default:
                    listTrajets(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace(); 
            request.setAttribute("errorMessage", "Une erreur est survenue lors du traitement des trajets.");
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
            insertUpdateTrajet(request, response, false);
        } else if ("update".equals(action)) {
            insertUpdateTrajet(request, response, true);
        } else {
             listTrajets(request, response);
        }
    }

    private void listTrajets(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Trajet> listTrajet = trajetDAO.findAll();
        request.setAttribute("listTrajet", listTrajet);
        request.getRequestDispatcher("/WEB-INF/jsp/admin/trajets/listTrajets.jsp").forward(request, response);
    }

    private void showNewEditForm(HttpServletRequest request, HttpServletResponse response, boolean isEdit) throws ServletException, IOException {
        List<Gare> listGare = gareDAO.findAll(); 
        request.setAttribute("listGare", listGare);

        if (isEdit) {
            try {
                Long id = Long.parseLong(request.getParameter("id"));
                Optional<Trajet> existingTrajet = trajetDAO.findById(id);
                if (existingTrajet.isPresent()) {
                    request.setAttribute("trajet", existingTrajet.get());
                    request.setAttribute("formAction", "update");
                } else {
                    request.getSession().setAttribute("errorMessage", "Trajet non trouvé pour modification.");
                    response.sendRedirect(request.getContextPath() + "/admin/trajets?action=list");
                    return;
                }
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("errorMessage", "ID de trajet invalide.");
                response.sendRedirect(request.getContextPath() + "/admin/trajets?action=list");
                return;
            }
        } else {
            request.setAttribute("trajet", new Trajet()); 
            request.setAttribute("formAction", "insert");
        }
        request.getRequestDispatcher("/WEB-INF/jsp/admin/trajets/formTrajet.jsp").forward(request, response);
    }

    private void insertUpdateTrajet(HttpServletRequest request, HttpServletResponse response, boolean isUpdate) throws IOException, ServletException {
        String description = request.getParameter("description");
        String gareDepartIdParam = request.getParameter("gareDepartId");
        String gareArriveeIdParam = request.getParameter("gareArriveeId");
        String idParam = request.getParameter("id"); 
        Long id = null;
        Long gareDepartId = null;
        Long gareArriveeId = null;

        Trajet currentTrajet = isUpdate ? trajetDAO.findById(Long.parseLong(idParam)).orElse(new Trajet()) : new Trajet();
        if (isUpdate) {
             try {
                 id = Long.parseLong(idParam);
                 currentTrajet.setId(id);
             } catch (NumberFormatException e) {
                 request.getSession().setAttribute("errorMessage", "ID de trajet invalide pour la mise à jour.");
                 response.sendRedirect(request.getContextPath() + "/admin/trajets?action=list");
                 return;
             }
        }
        
        try {
            gareDepartId = Long.parseLong(gareDepartIdParam);
            gareArriveeId = Long.parseLong(gareArriveeIdParam);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Veuillez sélectionner une gare de départ et d\'arrivée valides.");
            request.setAttribute("trajet", currentTrajet); 
            request.setAttribute("formAction", isUpdate ? "update" : "insert");
            request.setAttribute("listGare", gareDAO.findAll()); 
            request.getRequestDispatcher("/WEB-INF/jsp/admin/trajets/formTrajet.jsp").forward(request, response);
            return;
        }

        if (gareDepartId.equals(gareArriveeId)) {
            request.setAttribute("errorMessage", "La gare de départ et la gare d\'arrivée ne peuvent pas être identiques.");
            request.setAttribute("trajet", currentTrajet); 
            request.setAttribute("formAction", isUpdate ? "update" : "insert");
            request.setAttribute("listGare", gareDAO.findAll()); 
            request.getRequestDispatcher("/WEB-INF/jsp/admin/trajets/formTrajet.jsp").forward(request, response);
            return;
        }

        // --- Fetch Gare entities --- 
        Optional<Gare> gareDepartOpt = gareDAO.findById(gareDepartId);
        Optional<Gare> gareArriveeOpt = gareDAO.findById(gareArriveeId);

        if (!gareDepartOpt.isPresent() || !gareArriveeOpt.isPresent()) {
            request.setAttribute("errorMessage", "Gare de départ ou d\'arrivée invalide.");
            request.setAttribute("trajet", currentTrajet); 
            request.setAttribute("formAction", isUpdate ? "update" : "insert");
            request.setAttribute("listGare", gareDAO.findAll());
            request.getRequestDispatcher("/WEB-INF/jsp/admin/trajets/formTrajet.jsp").forward(request, response);
            return;
        }

        currentTrajet.setGareDepart(gareDepartOpt.get());
        currentTrajet.setGareArrivee(gareArriveeOpt.get());
        currentTrajet.setDescription(description != null ? description.trim() : null);

        try {
            if (isUpdate) {
                trajetDAO.update(currentTrajet);
                request.getSession().setAttribute("successMessage", "Trajet mis à jour avec succès !");
            } else {
                trajetDAO.save(currentTrajet);
                request.getSession().setAttribute("successMessage", "Trajet ajouté avec succès !");
            }
            response.sendRedirect(request.getContextPath() + "/admin/trajets?action=list");
        } catch (Exception e) {
            e.printStackTrace(); 
            request.setAttribute("errorMessage", "Erreur lors de la sauvegarde du trajet: " + e.getMessage());
            request.setAttribute("trajet", currentTrajet);
            request.setAttribute("formAction", isUpdate ? "update" : "insert");
            request.setAttribute("listGare", gareDAO.findAll());
            request.getRequestDispatcher("/WEB-INF/jsp/admin/trajets/formTrajet.jsp").forward(request, response);
        }
    }

    private void deleteTrajet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            trajetDAO.delete(id);
            request.getSession().setAttribute("successMessage", "Trajet supprimé avec succès !");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID de trajet invalide pour la suppression.");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Erreur lors de la suppression du trajet. Il est peut-être utilisé ailleurs.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/trajets?action=list");
    }
}

