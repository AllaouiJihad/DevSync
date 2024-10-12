package org.example.repository.implementation;

import jakarta.persistence.*;
import org.example.model.entities.Task;
import org.example.model.entities.User;
import org.example.repository.interfaces.TaskRepository;
import org.example.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskRepositoryImpl implements TaskRepository {

    EntityManagerFactory entityManagerFactory;

    private UserRepositoryImpl userRepository = new UserRepositoryImpl();
    public TaskRepositoryImpl() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("DevSyncPU");;
    }
    @Override
    public Task save(Task task) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(task);
            entityManager.getTransaction().commit();
            return task; // Return the persisted task
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.out.println("Error saving task: " + e.getMessage());
            return null;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<Task> findById(Long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return Optional.ofNullable(entityManager.find(Task.class, id));
        }
    }

    @Override
    public Task update(Task task) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(task);
            entityManager.getTransaction().commit();
            return task;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.out.println("Error updating task: " + e.getMessage());
            return null;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void delete(Task task) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            if (entityManager.contains(task)) {
                entityManager.remove(task);
            } else {
                entityManager.remove(entityManager.merge(task));
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.out.println("Error deleting task: " + e.getMessage());
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Task> getAll() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            TypedQuery<Task> query = entityManager.createQuery("SELECT t FROM Task t ORDER BY t.id", Task.class);
            return query.getResultList();
        }
    }

    @Override
    public List<Task> getTasksByAssigneeId(Long userId) {
        List<Task> tasks = new ArrayList<>();
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();

            try {
                User assignedUser = userRepository.findById(userId).orElse(null);

                if (assignedUser != null) {
                    tasks = entityManager.createQuery("SELECT t FROM Task t WHERE t.assignedTo = :assignedTo", Task.class)
                            .setParameter("assignedTo", assignedUser)
                            .getResultList();

                }
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tasks;
    }

    @Override
    public List<Task> getTasksByCreatorId(Long userId) {
        return null;
    }

    @Override
    public List<Task> findOverdueTasks(LocalDate date) {
        return null;
    }

    @Override
    public List<Task> findByTagsAndDateRangeAndCreator(String tag, LocalDateTime startDate, LocalDateTime endDate, Long creatorId) {
        return null;
    }
}
