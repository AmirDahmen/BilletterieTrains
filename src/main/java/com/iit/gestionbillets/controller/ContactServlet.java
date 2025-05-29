package com.iit.gestionbillets.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/contact")
public class ContactServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String success = request.getParameter("success");
        if ("true".equals(success)) {
            request.setAttribute("successMessage", "Votre message a été envoyé avec succès ! Nous vous répondrons dans les plus brefs délais.");
        }
        
        request.getRequestDispatcher("/WEB-INF/jsp/contact.jsp").forward(request, response);
    }
}