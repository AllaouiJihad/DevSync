package org.example.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.model.entities.Tag;
import org.example.model.entities.Task;
import org.example.model.entities.User;
import org.example.model.enums.Status;
import org.example.model.enums.UserRole;
import org.example.repository.implementation.TagRepositoryImpl;
import org.example.repository.implementation.TaskRepositoryImpl;
import org.example.service.TagService;
import org.example.service.TaskService;
import org.example.service.UserService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/tasks")
public class TaskServlet extends HttpServlet {

    private UserService userService = new UserService();
    private TaskService taskService = new TaskService();

    private TagService tagService = new TagService();

    @Override
    public void init() throws ServletException {
        System.out.println("TaskWebService initialized.");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        try {
            if ("edit".equals(action)) {
                Long taskId = Long.valueOf(request.getParameter("id"));
                Task task = taskService.getTaskById(taskId);
                if (task != null) {
                    List<Tag> tags=tagService.getAll();
                    List<User> utilisateurList=userService.getAllUsers();
                    request.setAttribute("task", task);
                    request.setAttribute("tags", tags);
                    request.setAttribute("currentUser", currentUser);
                    request.setAttribute("utilisateurList", utilisateurList);
                    request.getRequestDispatcher("/WEB-INF/views/Task/editTask.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Task not found");
                }
            } else {
                List<Tag> tags=tagService.getAll();
                List<User> utilisateurList=userService.getAllUsers();
                List<Task> myTasks = taskService.getTasksByCreator(currentUser.getId());
                String role=currentUser.getRole().name();
                request.setAttribute("myTasks", myTasks);
                request.setAttribute("tags", tags);
                request.getSession().setAttribute("role", role);
                request.setAttribute("currentUser", currentUser);
                request.setAttribute("utilisateurList", utilisateurList);
                request.getRequestDispatcher("/WEB-INF/views/Task/tasks.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("An error occurred: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String action = req.getParameter("action");

        try {
            if ("add".equals(action)) {
                String title = req.getParameter("title");
                String description = req.getParameter("description");
                String statusStr = req.getParameter("status");
                String deadLineStr = req.getParameter("deadLine");

                LocalDateTime deadLine = LocalDateTime.parse(deadLineStr);
                Status status = Status.valueOf(statusStr);

                HttpSession session = req.getSession();
                User currentUser = (User) session.getAttribute("currentUser");
                Task task = new Task();
                if(currentUser.getRole() == UserRole.MANAGER){

                    long assignedTo = Long.parseLong(req.getParameter("assignedTo"));
                    User assignedToUser = userService.getUserById(assignedTo).orElse(null);
                    task.setAssignedTo(assignedToUser);
                } else if (currentUser.getRole()== UserRole.USER) {
                    task.setAssignedTo(currentUser);
                }
                task.setTitle(title);
                task.setDescription(description);
                task.setStatus(status);
                task.setDeadLine(deadLine);
                task.setCreatedBy(currentUser);
                String[] tagIds = req.getParameterValues("tags");
                List<Tag> tags = new ArrayList<>();

                if (tagIds != null && tagIds.length > 0) {
                    for (String tagId : tagIds) {
                        Long id = Long.parseLong(tagId);
                        Tag tag = tagService.findById(id).orElse(null);
                        tags.add(tag);
                    }
                }
                task.setTags(tags);
                taskService.createTask(task);
                res.sendRedirect(req.getContextPath() + "/tasks");

            } else if ("update".equals(action)) {
                Long taskId = Long.valueOf(req.getParameter("id"));
                String title = req.getParameter("title");
                String description = req.getParameter("description");
                String statusStr = req.getParameter("status");
                String deadLineStr = req.getParameter("deadLine");
                HttpSession session = req.getSession();
                User currentUser = (User) session.getAttribute("currentUser");
                Task task = taskService.getTaskById(taskId);
                if (task != null) {
                    if(currentUser.getRole()== UserRole.MANAGER){

                        long assignedTo = Long.parseLong(req.getParameter("assignedTo"));
                        User assignedToUser = userService.getUserById(assignedTo).orElse(null);
                        task.setAssignedTo(assignedToUser);
                    } else if (currentUser.getRole()== UserRole.USER) {
                        task.setAssignedTo(currentUser);
                    }
                    task.setTitle(title);
                    task.setDescription(description);
                    task.setStatus(Status.valueOf(statusStr));
                    task.setDeadLine(LocalDateTime.parse(deadLineStr));
                    task.setCreatedBy(currentUser);
                    String[] tagIds = req.getParameterValues("tags");
                    List<Tag> tags = new ArrayList<>();

                    if (tagIds != null && tagIds.length > 0) {
                        for (String tagId : tagIds) {
                            Long id = Long.parseLong(tagId);
                            Tag tag = tagService.findById(id).orElse(null);
                            tags.add(tag);
                        }
                    }
                    task.setTags(tags);
                    taskService.updateTask(task);
                }
                res.sendRedirect(req.getContextPath() + "/tasks");
            } else if ("delete".equals(action)) {
                Long taskId = Long.valueOf(req.getParameter("id"));
                Task task = taskService.getTaskById(taskId);
                taskService.deleteTask(task);
                res.sendRedirect(req.getContextPath() + "/tasks");
            }else if ("changeStatus".equals(action)) {
                Long taskId = Long.valueOf(req.getParameter("id"));
                Status newStatus = Status.valueOf(req.getParameter("status"));
                taskService.changeStatus(taskId, newStatus);
                res.sendRedirect(req.getContextPath() + "/profil");
            }

        } catch (Exception e) {
            e.printStackTrace();
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            res.getWriter().write("An error occurred: " + e.getMessage());
        }
    }


}
