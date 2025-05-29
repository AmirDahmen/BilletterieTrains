package com.iit.gestionbillets.controller;

import com.iit.gestionbillets.dao.Interface.IGareDAO;
import com.iit.gestionbillets.dao.impl.GareDAOImpl;
import com.iit.gestionbillets.model.Gare;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private IGareDAO gareDAO;

    @Override
    public void init() throws ServletException {
        this.gareDAO = new GareDAOImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Gare> listGare = gareDAO.findAll();
        request.setAttribute("listGare", listGare);
        
        request.getRequestDispatcher("/WEB-INF/jsp/public/index.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}

