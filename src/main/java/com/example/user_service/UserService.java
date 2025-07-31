package com.example.user_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User loginUser(String username, String password) {
        User user = userRepository.findByEmail(username);

        
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null; 
    }
    
    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public User findByUsername(String username) {
        return userRepository.findByEmail(username); 
    }
}
