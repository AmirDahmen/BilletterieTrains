package com.iit.gestionbillets.controller;

import com.iit.gestionbillets.dao.Interface.IBilletDAO;
import com.iit.gestionbillets.dao.Interface.IVoyageDAO;
import com.iit.gestionbillets.dao.impl.BilletDAOImpl;
import com.iit.gestionbillets.dao.impl.VoyageDAOImpl;
import com.iit.gestionbillets.model.Billet;
import com.iit.gestionbillets.model.Role;
import com.iit.gestionbillets.model.User;
import com.iit.gestionbillets.model.Voyage;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class VoyageursServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private IBilletDAO billetDAO;
    private IVoyageDAO voyageDAO;
    
    @Override
    public void init( ) throws ServletException {
        this.billetDAO = new BilletDAOImpl();
        this.voyageDAO = new VoyageDAOImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("user") == null || 
            ((User) session.getAttribute("user")).getRole() != Role.ADMIN) {
            response.sendRedirect(request.getContextPath() + "/auth?action=showLogin");
            return;
        }
        
        String voyageIdParam = request.getParameter("voyageId");
        if (voyageIdParam == null || voyageIdParam.isEmpty()) {
            session.setAttribute("errorMessage", "ID de voyage non spécifié.");
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            return;
        }
        
        try {
            Long voyageId = Long.parseLong(voyageIdParam);
            
            Optional<Voyage> voyageOpt = voyageDAO.findById(voyageId);
            if (!voyageOpt.isPresent()) {
                session.setAttribute("errorMessage", "Voyage non trouvé.");
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                return;
            }
            
            Voyage voyage = voyageOpt.get();
            
            List<Billet> billets = billetDAO.findByVoyageId(voyageId);
            
            request.setAttribute("voyage", voyage);
            request.setAttribute("billets", billets);
            
            request.getRequestDispatcher("/WEB-INF/jsp/admin/voyages/listeVoyageurs.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "ID de voyage invalide.");
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
