package org.example.repository.interfaces;

import org.example.model.entities.UserToken;

import java.util.List;
import java.util.Optional;

public interface UserTokenRepository {
    UserToken save(UserToken userToken);
    Optional<UserToken> findById(Long id);
    List<UserToken> findAll();
    UserToken update(UserToken userToken);
    void delete(long id);
    List<UserToken>findByTokenType(String tokenType);
    UserToken findByUser(Long userId);
    UserToken findByUserAndType(Long userId, String tokenType);
}
