package org.example.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.model.entities.Task;
import org.example.model.entities.TaskHistory;
import org.example.model.entities.User;
import org.example.model.enums.UserRole;
import org.example.service.TaskHistoryService;
import org.example.service.TaskService;
import org.example.service.UserService;

import java.io.IOException;
import java.util.List;

@WebServlet("/profil")
public class ProfilServlet extends HttpServlet {

    private TaskService taskService;
    private TaskHistoryService taskHistoryService;

    @Override
    public void init() throws ServletException {
        UserService userService = new UserService();
        taskHistoryService=new TaskHistoryService();
        taskService=new TaskService();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser != null) {
            if (currentUser.getRole() == UserRole.MANAGER) {
                List<Task> myTasks = taskService.getTasksByCreator(currentUser.getId());
                List<TaskHistory> TasksToChange = taskHistoryService.getMyRequestToApproved(currentUser);
                request.setAttribute("currentUser", currentUser);
                request.setAttribute("myTasks", myTasks);
                request.setAttribute("TasksToChange", TasksToChange);
                request.getRequestDispatcher("/WEB-INF/views/profileAdmin.jsp").forward(request, response);

            } else if (currentUser.getRole() == UserRole.USER) {
                List<Task> myTasks = taskService.getTasksByAssigned(currentUser.getId());
                request.setAttribute("currentUser", currentUser);
                request.setAttribute("myTasks", myTasks);
                request.getRequestDispatcher("/WEB-INF/views/profil.jsp").forward(request, response);

            }
        }
        else {
            response.sendRedirect("/login");
        }
    }
}
