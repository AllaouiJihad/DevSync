package org.example.repository.implementation;

import jakarta.persistence.*;
import org.example.model.entities.Tag;
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
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                updateTasksTags(task, entityManager);
                entityManager.persist(task);
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw new RuntimeException(e);
            }
            return task;
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
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                updateTasksTags(task, entityManager);
                Task updatedTask = entityManager.merge(task);
                transaction.commit();
                return updatedTask;
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw new RuntimeException(e);
            }
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
        List<Task> tasks = new ArrayList<>();
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();

            try {
                User cretatedUser = userRepository.findById(userId).orElse(null);

                if (cretatedUser != null) {
                    tasks = entityManager.createQuery("SELECT t FROM Task t WHERE t.createdBy = :createBy", Task.class)
                            .setParameter("createBy", cretatedUser)
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
    public List<Task> findOverdueTasks(LocalDate date) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery("SELECT t FROM Task t WHERE t.deadLine <= :date AND t.status!= 'TERMINEE'", Task.class)
                    .setParameter("date", date)
                    .getResultList();
        }
    }

    @Override
    public List<Task> findByTagsAndDateRangeAndCreator(String tag, LocalDateTime startDate, LocalDateTime endDate, Long creatorId) {
        List<Task> tasks = new ArrayList<>();
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();

            try {
                tasks = entityManager.createQuery(
                                "SELECT t FROM Task t JOIN t.tags tg " +
                                        "WHERE tg.name = :tag " +
                                        "AND t.createdBy.id = :creatorId " +
                                        "AND t.deadLine BETWEEN :startDate AND :endDate", Task.class)
                        .setParameter("tag", tag)
                        .setParameter("creatorId", creatorId)
                        .setParameter("startDate", startDate)
                        .setParameter("endDate", endDate)
                        .getResultList();

                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tasks;
    }

    private void updateTasksTags(Task task, EntityManager entityManager) {
        if (task.getTags() != null) {
            for (int i = 0; i < task.getTags().size(); i++) {
                Tag tag = task.getTags().get(i);
                if (tag.getId() != null) {
                    task.getTags().set(i, entityManager.merge(tag));
                } else {
                    entityManager.persist(tag);
                }
            }
        }
    }


}
