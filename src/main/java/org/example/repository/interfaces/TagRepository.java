package org.example.repository.interfaces;

import org.example.model.entities.Tag;
import org.example.model.entities.User;

import java.util.List;
import java.util.Optional;

public interface TagRepository {
    Tag save(Tag tag);

    Optional<Tag> findById(Long id);

    List<Tag> findAll();

    Tag update(Tag tag);
    void delete(Long id);

    List<Tag> findByName(String name);
}
