package org.example.service;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.model.entities.Tag;
import org.example.repository.implementation.TagRepositoryImpl;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;


public class TagService {
    TagRepositoryImpl tagRepository;

    public TagService() {

        this.tagRepository = new TagRepositoryImpl();
    }

    public Tag addTag(Tag tag){
        return tagRepository.save(tag);
    }

    public Optional<Tag> findById(Long id){
        return tagRepository.findById(id);
    }

    public List<Tag> getAll(){
        return tagRepository.findAll();
    }

}
