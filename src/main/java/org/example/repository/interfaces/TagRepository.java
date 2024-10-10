package org.example.repository.interfaces;

import org.example.model.entities.Tag;
import org.example.model.entities.User;

import java.util.List;

public interface TagRepository {
    void save(Tag tag);

    Tag findById(Long id);

    List<Tag> findAll();
}
