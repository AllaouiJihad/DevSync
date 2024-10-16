package org.example.repository.implementation;

import jakarta.persistence.*;
import org.example.model.entities.User;
import org.example.repository.interfaces.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    EntityManagerFactory entityManagerFactory;

    public UserRepositoryImpl() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("DevSyncPU");
    }



    @Override
    public void save(User user) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(user);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.out.println(e.getMessage());
        }finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            User user = entityManager.find(User.class, id);

            return Optional.ofNullable(user);
        }
    }

    @Override
    public List<User> findAll() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            TypedQuery<User> query = entityManager.createQuery("select u from User u ORDER BY u.id", User.class);
            return query.getResultList();
        }
    }

    @Override
    public void update(User user) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(user);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }


    @Override
    public void delete(User user) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            if (entityManager.contains(user)) {
                entityManager.remove(user);
            } else {
                entityManager.remove(entityManager.merge(user));
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.out.println(e.getMessage());
        } finally {
            entityManager.close();
        }
    }

    public User findByEmail(String email) {
        EntityManager em = entityManagerFactory.createEntityManager();
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        try{
            return query.getSingleResult();
        }catch (NoResultException e){
            return null;
        }finally {
            em.close();
        }
    }

}
