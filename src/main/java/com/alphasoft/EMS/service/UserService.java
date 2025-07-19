package com.alphasoft.EMS.service;


import com.alphasoft.EMS.enums.Role;
import com.alphasoft.EMS.model.User;
import com.alphasoft.EMS.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public void delete(User user) {
        userRepository.delete(user);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public boolean isAdmin(Long id) {
        User user = findUserById(id);
        return user != null && user.getRole() == Role.ADMIN;
    }

    public boolean isActive(Long id) {
        User user = findUserById(id);
        return user != null && user.isActive();
    }
}
