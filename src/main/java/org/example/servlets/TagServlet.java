package org.example.servlets;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.entities.Tag;
import org.example.model.entities.User;
import org.example.repository.implementation.TagRepositoryImpl;
import org.example.repository.interfaces.TagRepository;
import org.example.service.TagService;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

@WebServlet("/tags")
public class TagServlet extends HttpServlet {


    TagRepositoryImpl tagRepository= new TagRepositoryImpl();
    TagService tagService = new TagService(tagRepository);


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("list".equals(action)){
            try {
                listTags(request,response);
            } catch (Exception e) {
                e.printStackTrace(); // Imprime la trace de l'exception dans les logs du serveur
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Une erreur s'est produite lors du traitement de votre demande.");
            }
        }
        else {
            listTags(request,response);
        }


    }
    private void listTags(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Tag> tags = tagService.getAll();
        request.setAttribute("tags", tags);
        request.getRequestDispatcher("/WEB-INF/views/tags.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("add".equals(action)) {
            try {
                addTag(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Une erreur s'est produite lors de l'ajout du tag.");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/tags?action=list");
        }
    }

    private void addTag(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        Tag tag = new Tag();
        tag.setName(name);

        tagService.addTag(tag);
        response.sendRedirect(request.getContextPath() + "/tags?action=list");
    }
}