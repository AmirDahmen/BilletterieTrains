package com.iit.gestionbillets.controller;

import com.iit.gestionbillets.dao.Interface.IBilletDAO;
import com.iit.gestionbillets.dao.impl.BilletDAOImpl;
import com.iit.gestionbillets.model.Billet;
import com.iit.gestionbillets.model.User;
import com.iit.gestionbillets.model.Role;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

public class ClientDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private IBilletDAO billetDAO;

    @Override
    public void init() throws ServletException {
        this.billetDAO = new BilletDAOImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=showLogin");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user.getRole() != Role.CLIENT) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            return;
        }

        // Fetch la liste de tickeeets
        List<Billet> mesBillets = billetDAO.findByUserId(user.getId());
        request.setAttribute("mesBillets", mesBillets);

        request.getRequestDispatcher("/WEB-INF/jsp/user/dashboardClient.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}

