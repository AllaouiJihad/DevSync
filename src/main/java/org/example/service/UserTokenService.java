package org.example.service;

import org.example.model.entities.User;
import org.example.model.entities.UserToken;
import org.example.repository.implementation.UserTokenRepositoryImpl;

import java.util.List;
import java.util.Optional;

public class UserTokenService {

    UserTokenRepositoryImpl userTokenRepository;

    public UserTokenService() {
        this.userTokenRepository = new UserTokenRepositoryImpl();
    }

    public void save(UserToken token) {
        userTokenRepository.save(token);
    }
    public Optional<UserToken> findByToken(long token) {
        return userTokenRepository.findById(token);
    }
    public void deleteByToken(long token) {
        userTokenRepository.delete(token);
    }
    public List<UserToken> findByTokenType(String tokenType) {
        return (List<UserToken>) userTokenRepository.findByTokenType(tokenType);
    }
    public void update(UserToken token){
        userTokenRepository.update(token);
    }
    public UserToken findByUser(User user) {
        return userTokenRepository.findByUser(user.getId());
    }
    public UserToken findByUserAndTokenType(User user, String tokenType) {
        return userTokenRepository.findByUserAndType(user.getId(), tokenType);
    }
}
