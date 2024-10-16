package org.example.repository.implementation;

import jakarta.persistence.*;
import org.example.model.entities.Tag;
import org.example.model.entities.User;
import org.example.repository.interfaces.TagRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class TagRepositoryImpl implements TagRepository {

    EntityManagerFactory entityManagerFactory;

    public TagRepositoryImpl() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("DevSyncPU");
    }

    @Override
    public Tag save(Tag tag) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(tag);
            entityManager.flush();
            Tag savedTag = entityManager.find(Tag.class,tag.getId());
            entityManager.getTransaction().commit();
            return savedTag;
        }catch (Exception e){
            if (entityManager.getTransaction().isActive()){
                entityManager.getTransaction().rollback();
            }
            System.out.println(e.getMessage());
            return null;
        }finally {
            entityManager.close();
        }

    }

    @Override
    public Optional<Tag> findById(Long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            Tag tag = entityManager.find(Tag.class, id);
            return Optional.ofNullable(tag);
        }
    }

    @Override
    public List<Tag> findAll() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()){
            TypedQuery<Tag> query = entityManager.createQuery("select t from Tag t order by t.id",Tag.class);
            return query.getResultList();

        }
    }

    @Override
    public Tag update(Tag tag) {
        try(EntityManager entityManager= entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction=entityManager.getTransaction();
            try{
                transaction.begin();
                Tag updatedTag=entityManager.merge(tag);
                transaction.commit();
                return updatedTag;
            } catch (Exception e) {
                if(transaction.isActive()){
                    transaction.rollback();
                }
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void delete(Long id) {
        try (EntityManager entityManager= entityManagerFactory.createEntityManager()){
            EntityTransaction transaction=entityManager.getTransaction();
            try {
                transaction.begin();
                Tag tag=entityManager.find(Tag.class, id);
                if(tag != null){
                    entityManager.remove(tag);
                }
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()){
                    transaction.rollback();
                }
                throw new RuntimeException(e);
            }

        }

    }

    @Override
    public List<Tag> findByName(String name) {
        return List.of();
    }


}
