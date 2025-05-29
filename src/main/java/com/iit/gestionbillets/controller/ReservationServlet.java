package com.iit.gestionbillets.controller;

import com.iit.gestionbillets.dao.Interface.IBilletDAO;
import com.iit.gestionbillets.dao.Interface.IVoyageDAO;
import com.iit.gestionbillets.dao.impl.BilletDAOImpl;
import com.iit.gestionbillets.dao.impl.VoyageDAOImpl;
import com.iit.gestionbillets.model.Billet;
import com.iit.gestionbillets.model.StatutBillet;
import com.iit.gestionbillets.model.User;
import com.iit.gestionbillets.model.Voyage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class ReservationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private IVoyageDAO voyageDAO;
    private IBilletDAO billetDAO;

    @Override
    public void init() throws ServletException {
        this.voyageDAO = new VoyageDAOImpl();
        this.billetDAO = new BilletDAOImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // 1. Check l'authentificatioon
        if (session == null || session.getAttribute("user") == null) {
            String queryString = request.getQueryString();
            String redirectUrl = request.getRequestURI() + (queryString != null ? "?" + queryString : "");
            response.sendRedirect(request.getContextPath() + "/auth?action=showLogin&redirect=" + response.encodeURL(redirectUrl));
            return;
        }

        String action = request.getParameter("action");

        if ("confirm".equals(action)) {
            processReservation(request, response, session);
        } else {
            response.sendRedirect(request.getContextPath() + "/client/dashboard"); 
        }
    }

    private void processReservation(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
        User user = (User) session.getAttribute("user");
        String voyageIdParam = request.getParameter("voyageId");
        Long voyageId = null;

        // 2. Validate Voyage ID
        try {
            voyageId = Long.parseLong(voyageIdParam);
        } catch (NumberFormatException | NullPointerException e) {
            session.setAttribute("reservationError", "ID de voyage invalide.");
            response.sendRedirect(request.getContextPath() + "/client/dashboard"); 
            return;
        }

        // 3. Fetch Voyage and Check Availability 
        Optional<Voyage> voyageOpt = voyageDAO.findById(voyageId);

        if (!voyageOpt.isPresent()) {
            session.setAttribute("reservationError", "Le voyage sélectionné n\'existe plus.");
            response.sendRedirect(request.getContextPath() + "/client/dashboard");
            return;
        }

        Voyage voyage = voyageOpt.get();

        if (voyage.getPlacesDisponibles() <= 0) {
            session.setAttribute("reservationError", "Désolé, il n\'y a plus de places disponibles pour ce voyage.");
             response.sendRedirect(request.getContextPath() + "/client/dashboard"); 
            return;
        }

        // 4. Create Billet
        try {
            
            // Decrement places and update voyage
            voyage.setPlacesDisponibles(voyage.getPlacesDisponibles() - 1);
            voyageDAO.update(voyage); 

            // Create and save the billet
            String reference = "BILLET-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            BigDecimal prixPaye = voyage.getPrix(); 
            Billet newBillet = new Billet(user, voyage, prixPaye, reference);
            newBillet.setStatut(StatutBillet.CONFIRMED);
            newBillet.setDateReservation(LocalDateTime.now());
            
            billetDAO.save(newBillet);


            // 5. Redirect to Billet Display Page (Step 004)
            session.setAttribute("reservationSuccess", "Votre réservation pour le voyage " + voyage.getTrajet().getGareDepart().getNom() + " -> " + voyage.getTrajet().getGareArrivee().getNom() + " a été confirmée !");
            response.sendRedirect(request.getContextPath() + "/billet?action=view&ref=" + newBillet.getReferenceBillet());

        } catch (Exception e) {
            
            e.printStackTrace();
            session.setAttribute("reservationError", "Une erreur technique est survenue lors de la réservation. Veuillez réessayer.");
            
            response.sendRedirect(request.getContextPath() + "/client/dashboard");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}

