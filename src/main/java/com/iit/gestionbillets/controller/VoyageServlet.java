package com.iit.gestionbillets.controller;

import com.iit.gestionbillets.dao.Interface.ITrajetDAO;
import com.iit.gestionbillets.dao.Interface.IVoyageDAO;
import com.iit.gestionbillets.dao.impl.TrajetDAOImpl;
import com.iit.gestionbillets.dao.impl.VoyageDAOImpl;
import com.iit.gestionbillets.model.Trajet;
import com.iit.gestionbillets.model.Voyage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

public class VoyageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private IVoyageDAO voyageDAO;
    private ITrajetDAO trajetDAO; 

    @Override
    public void init() throws ServletException {
        this.voyageDAO = new VoyageDAOImpl();
        this.trajetDAO = new TrajetDAOImpl();
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
                    deleteVoyage(request, response);
                    break;
                case "list":
                default:
                    listVoyages(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace(); 
            request.setAttribute("errorMessage", "Une erreur est survenue lors du traitement des voyages.");
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
            insertUpdateVoyage(request, response, false);
        } else if ("update".equals(action)) {
            insertUpdateVoyage(request, response, true);
        } else {
             listVoyages(request, response); 
        }
    }

    private void listVoyages(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Voyage> listVoyage = voyageDAO.findAll();
        request.setAttribute("listVoyage", listVoyage);
        request.getRequestDispatcher("/WEB-INF/jsp/admin/voyages/listVoyages.jsp").forward(request, response);
    }

    private void showNewEditForm(HttpServletRequest request, HttpServletResponse response, boolean isEdit) throws ServletException, IOException {
        List<Trajet> listTrajet = trajetDAO.findAll(); 
        request.setAttribute("listTrajet", listTrajet);

        if (isEdit) {
            try {
                Long id = Long.parseLong(request.getParameter("id"));
                Optional<Voyage> existingVoyage = voyageDAO.findById(id);
                if (existingVoyage.isPresent()) {
                    request.setAttribute("voyage", existingVoyage.get());
                    request.setAttribute("formAction", "update");
                } else {
                    request.getSession().setAttribute("errorMessage", "Voyage non trouvé pour modification.");
                    response.sendRedirect(request.getContextPath() + "/admin/voyages?action=list");
                    return;
                }
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("errorMessage", "ID de voyage invalide.");
                response.sendRedirect(request.getContextPath() + "/admin/voyages?action=list");
                return;
            }
        } else {
            request.setAttribute("voyage", new Voyage()); 
            request.setAttribute("formAction", "insert");
        }
        request.getRequestDispatcher("/WEB-INF/jsp/admin/voyages/formVoyage.jsp").forward(request, response);
    }

    private void insertUpdateVoyage(HttpServletRequest request, HttpServletResponse response, boolean isUpdate) throws IOException, ServletException {
        String trajetIdParam = request.getParameter("trajetId");
        String heureDepartStr = request.getParameter("heureDepart"); 
        String heureArriveeStr = request.getParameter("heureArrivee"); 
        String prixStr = request.getParameter("prix");
        String placesDisponiblesStr = request.getParameter("placesDisponibles");
        String idParam = request.getParameter("id"); 
        Long id = null;
        Long trajetId = null;
        LocalDateTime heureDepart = null;
        LocalDateTime heureArrivee = null;
        BigDecimal prix = null;
        int placesDisponibles = 0;

        Voyage currentVoyage = new Voyage(); 

        if (isUpdate) {
             try {
                 id = Long.parseLong(idParam);
                 currentVoyage.setId(id);
             } catch (NumberFormatException e) {
                 request.getSession().setAttribute("errorMessage", "ID de voyage invalide pour la mise à jour.");
                 response.sendRedirect(request.getContextPath() + "/admin/voyages?action=list");
                 return;
             }
        }

        StringBuilder errors = new StringBuilder();
        try {
            trajetId = Long.parseLong(trajetIdParam);
        } catch (NumberFormatException e) {
            errors.append("Trajet invalide. ");
        }
        try {
            heureDepart = LocalDateTime.parse(heureDepartStr);
            currentVoyage.setHeureDepart(heureDepart); 
        } catch (DateTimeParseException | NullPointerException e) {
            errors.append("Format heure de départ invalide (AAAA-MM-JJTHH:MM). ");
        }
        try {
            heureArrivee = LocalDateTime.parse(heureArriveeStr);
            currentVoyage.setHeureArrivee(heureArrivee); 
        } catch (DateTimeParseException | NullPointerException e) {
            errors.append("Format heure d\\'arrivée invalide (AAAA-MM-JJTHH:MM). ");
        }
        try {
            prix = new BigDecimal(prixStr);
            if (prix.compareTo(BigDecimal.ZERO) < 0) {
                 errors.append("Le prix doit être positif. ");
            }
            currentVoyage.setPrix(prix); 
        } catch (NumberFormatException | NullPointerException e) {
            errors.append("Format de prix invalide. ");
        }
        try {
            placesDisponibles = Integer.parseInt(placesDisponiblesStr);
             if (placesDisponibles < 0) {
                 errors.append("Le nombre de places doit être positif ou nul. ");
            }
            currentVoyage.setPlacesDisponibles(placesDisponibles); 
        } catch (NumberFormatException | NullPointerException e) {
            errors.append("Format nombre de places invalide. ");
        }

        if (heureDepart != null && heureArrivee != null && !heureArrivee.isAfter(heureDepart)) {
            errors.append("L\\'heure d\\'arrivée doit être postérieure à l\\'heure de départ. ");
        }

        Optional<Trajet> trajetOpt = Optional.empty();
        if (trajetId != null) {
             trajetOpt = trajetDAO.findById(trajetId);
             if (!trajetOpt.isPresent()) {
                 errors.append("Trajet sélectionné non trouvé. ");
             } else {
                 currentVoyage.setTrajet(trajetOpt.get()); 
             }
        }
        
        if (errors.length() > 0) {
            request.setAttribute("errorMessage", errors.toString());
            request.setAttribute("voyage", currentVoyage);
            request.setAttribute("formAction", isUpdate ? "update" : "insert");
            request.setAttribute("listTrajet", trajetDAO.findAll()); 
            request.getRequestDispatcher("/WEB-INF/jsp/admin/voyages/formVoyage.jsp").forward(request, response);
            return;
        }

        currentVoyage.setTrajet(trajetOpt.get());
        currentVoyage.setHeureDepart(heureDepart);
        currentVoyage.setHeureArrivee(heureArrivee);
        currentVoyage.setPrix(prix);
        currentVoyage.setPlacesDisponibles(placesDisponibles);

        try {
            if (isUpdate) {
                voyageDAO.update(currentVoyage);
                request.getSession().setAttribute("successMessage", "Voyage mis à jour avec succès !");
            } else {
                voyageDAO.save(currentVoyage);
                request.getSession().setAttribute("successMessage", "Voyage ajouté avec succès !");
            }
            response.sendRedirect(request.getContextPath() + "/admin/voyages?action=list");
        } catch (Exception e) {
            e.printStackTrace(); 
            request.setAttribute("errorMessage", "Erreur lors de la sauvegarde du voyage: " + e.getMessage());
            request.setAttribute("voyage", currentVoyage);
            request.setAttribute("formAction", isUpdate ? "update" : "insert");
            request.setAttribute("listTrajet", trajetDAO.findAll());
            request.getRequestDispatcher("/WEB-INF/jsp/admin/voyages/formVoyage.jsp").forward(request, response);
        }
    }

    private void deleteVoyage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            voyageDAO.delete(id);
            request.getSession().setAttribute("successMessage", "Voyage supprimé avec succès !");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID de voyage invalide pour la suppression.");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Erreur lors de la suppression du voyage. Il est peut-être utilisé ailleurs (billets existants ?).");
        }
        response.sendRedirect(request.getContextPath() + "/admin/voyages?action=list");
    }
}

