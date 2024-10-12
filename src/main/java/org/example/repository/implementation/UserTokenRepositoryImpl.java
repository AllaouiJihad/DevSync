package org.example.repository.implementation;

import jakarta.persistence.*;
import org.example.model.entities.UserToken;
import org.example.repository.interfaces.UserTokenRepository;

import java.util.List;
import java.util.Optional;

public class UserTokenRepositoryImpl implements UserTokenRepository {

    EntityManagerFactory entityManagerFactory;

    public UserTokenRepositoryImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("DevSyncPU");
    }

    @Override
    public UserToken save(UserToken userToken) {
        System.out.println(userToken);
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                entityManager.persist(userToken);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw new RuntimeException(e);
            }
            return userToken;
        }
    }

    @Override
    public Optional<UserToken> findById(Long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            UserToken userToken = entityManager.find(UserToken.class, id);
            return Optional.ofNullable(userToken);
        }
    }

    @Override
    public List<UserToken> findAll() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery("SELECT ut FROM UserToken ut", UserToken.class).getResultList(); // Récupère tous les jetons
        }
    }

    @Override
    public UserToken update(UserToken userToken) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                UserToken updatedToken = entityManager.merge(userToken);
                transaction.commit();
                return updatedToken;
            } catch (Exception e) {
                transaction.rollback();
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void delete(long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                UserToken userToken = entityManager.find(UserToken.class, id);
                if (userToken != null) {
                    entityManager.remove(userToken);
                }
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public List<UserToken> findByTokenType(String tokenType) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery("SELECT ut FROM UserToken ut WHERE ut.tokenType = :tokenType", UserToken.class)
                    .setParameter("tokenType", tokenType)
                    .getResultList();
        }
    }

    @Override
    public UserToken findByUser(Long userId) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            String jpql = "SELECT ut FROM UserToken ut WHERE ut.user.id = :userId";
            TypedQuery<UserToken> query = entityManager.createQuery(jpql, UserToken.class);
            query.setParameter("userId", userId);
            try {
                return query.getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        }
    }

    @Override
    public UserToken findByUserAndType(Long userId, String tokenType) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            String jpql = "SELECT ut FROM UserToken ut WHERE ut.user.id = :userId and ut.tokenType = :tokenType";
            TypedQuery<UserToken> query = entityManager.createQuery(jpql, UserToken.class);
            query.setParameter("userId", userId);
            query.setParameter("tokenType", tokenType);
            try {
                return query.getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        }
    }
}
