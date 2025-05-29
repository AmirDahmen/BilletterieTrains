package com.iit.gestionbillets.controller;

import com.iit.gestionbillets.dao.Interface.IGareDAO;
import com.iit.gestionbillets.dao.Interface.IVoyageDAO;
import com.iit.gestionbillets.dao.impl.GareDAOImpl;
import com.iit.gestionbillets.dao.impl.VoyageDAOImpl;
import com.iit.gestionbillets.model.Gare;
import com.iit.gestionbillets.model.Voyage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

public class RechercheServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private IVoyageDAO voyageDAO;
    private IGareDAO gareDAO; 

    @Override
    public void init() throws ServletException {
        this.voyageDAO = new VoyageDAOImpl();
        this.gareDAO = new GareDAOImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("search".equals(action)) {
            performSearch(request, response);
        } else {
            
            response.sendRedirect(request.getContextPath() + "/");
        }
    }

    private void performSearch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String gareDepartIdParam = request.getParameter("gareDepartId");
        String gareArriveeIdParam = request.getParameter("gareArriveeId");
        String dateVoyageParam = request.getParameter("dateVoyage");

        Long gareDepartId = null;
        Long gareArriveeId = null;
        LocalDate dateVoyage = null;
        StringBuilder errorMessage = new StringBuilder();

        try {
            gareDepartId = Long.parseLong(gareDepartIdParam);
        } catch (NumberFormatException | NullPointerException e) {
            errorMessage.append("Veuillez sélectionner une gare de départ valide. ");
        }
        try {
            gareArriveeId = Long.parseLong(gareArriveeIdParam);
        } catch (NumberFormatException | NullPointerException e) {
            errorMessage.append("Veuillez sélectionner une gare d\\'arrivée valide. ");
        }
        try {
            dateVoyage = LocalDate.parse(dateVoyageParam);
            if (dateVoyage.isBefore(LocalDate.now())) {
                 errorMessage.append("La date de voyage ne peut pas être dans le passé. ");
            }
        } catch (DateTimeParseException | NullPointerException e) {
            errorMessage.append("Veuillez sélectionner une date de voyage valide. ");
        }

        if (gareDepartId != null && gareArriveeId != null && gareDepartId.equals(gareArriveeId)) {
             errorMessage.append("La gare de départ et d\\'arrivée ne peuvent pas être identiques. ");
        }

        if (errorMessage.length() > 0) {

            request.getSession().setAttribute("searchError", errorMessage.toString());
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        List<Voyage> resultats = voyageDAO.findByCriteria(gareDepartId, gareArriveeId, dateVoyage);

        request.setAttribute("resultatsVoyage", resultats);
        request.setAttribute("dateRecherche", dateVoyage); 
        Optional<Gare> gareDepartOpt = gareDAO.findById(gareDepartId);
        Optional<Gare> gareArriveeOpt = gareDAO.findById(gareArriveeId);
        request.setAttribute("gareDepartNom", gareDepartOpt.map(Gare::getNom).orElse("Inconnue"));
        request.setAttribute("gareArriveeNom", gareArriveeOpt.map(Gare::getNom).orElse("Inconnue"));

        request.getRequestDispatcher("/WEB-INF/jsp/public/resultatsRecherche.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}

