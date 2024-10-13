package org.example.service;

import org.example.model.entities.User;
import org.example.repository.implementation.UserRepositoryImpl;
import org.example.repository.interfaces.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import java.util.List;
import java.util.Optional;

public class UserService {

    UserRepositoryImpl userRepository;

    public UserService() {

        this.userRepository = new UserRepositoryImpl();
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void updateUser(User user) {
        userRepository.update(user);
    }

    public void deleteUser(Long id) {
        Optional<User> user = getUserById(id);
        if (user.isPresent()) {
            userRepository.delete(user.get());
        }else {
            System.out.println("User not found");
        }
    }
    public String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }
    public User login(String email, String password) {
        User u= userRepository.findByEmail(email);
        if (u !=null && BCrypt.checkpw(password, u.getPassword())) {
            return u;
        }
        return null;
    }


}
