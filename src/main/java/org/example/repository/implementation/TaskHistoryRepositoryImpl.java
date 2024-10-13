package org.example.repository.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.example.model.entities.TaskHistory;
import org.example.repository.interfaces.TaskHistoryRepository;

import java.util.List;
import java.util.Optional;

public class TaskHistoryRepositoryImpl implements TaskHistoryRepository {

    EntityManagerFactory entityManagerFactory;

    public TaskHistoryRepositoryImpl() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("DevSyncPU");
    }

    @Override
    public TaskHistory save(TaskHistory taskHistory) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                entityManager.persist(taskHistory);
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw new RuntimeException(e);
            }
            return taskHistory;
        }
    }

    @Override
    public Optional<TaskHistory> findById(Long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            TaskHistory taskHistory = entityManager.find(TaskHistory.class, id);
            return Optional.ofNullable(taskHistory);
        }
    }

    @Override
    public List<TaskHistory> findAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        List<TaskHistory> taskHistories;

        try {
            transaction.begin();
            taskHistories = entityManager.createQuery("select h from TaskHistory h", TaskHistory.class).getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error retrieving task histories", e);
        } finally {
            entityManager.close();
        }

        return taskHistories;
    }

    @Override
    public TaskHistory update(TaskHistory taskHistory) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                TaskHistory updatedTaskHistory = entityManager.merge(taskHistory);
                transaction.commit();
                return updatedTaskHistory;
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public List<TaskHistory> getAllTaskHistory() {
        try(EntityManager entityManager = entityManagerFactory.createEntityManager()) {

            EntityTransaction transaction= entityManager.getTransaction();
            if(!transaction.isActive()){
                transaction.begin();
            }
            List<TaskHistory> taskHistoryList = entityManager
                    .createQuery("from TaskHistory th",TaskHistory.class)
                    .getResultList();
            taskHistoryList.forEach(taskHistory -> entityManager.refresh(taskHistory));
            transaction.commit();
            return taskHistoryList;
        }catch (Exception e){
//            if(transaction.isActive()) {
//                transaction.rollback();
//            }
            throw e;
        }
    }
}
