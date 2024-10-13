package org.example.servlets;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.model.entities.User;
import org.example.repository.implementation.UserRepositoryImpl;
import org.example.service.UserService;

import java.io.IOException;
@WebServlet("/login")

public class LoginServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Supprime l'attribut loginError pour s'assurer qu'il n'y a pas d'erreur lors du chargement de la page
        request.removeAttribute("loginError");
        request.getRequestDispatcher("/WEB-INF/views/signin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User currentUtilisateur = userService.login(username, password);

        if (currentUtilisateur != null) {
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", currentUtilisateur);
            session.setAttribute("username", username);
            response.sendRedirect(request.getContextPath() + "/profil");
        } else {
            request.setAttribute("loginError", "Invalid username or password.");
            request.getRequestDispatcher("/WEB-INF/views/signin.jsp").forward(request, response);
        }
    }
}
