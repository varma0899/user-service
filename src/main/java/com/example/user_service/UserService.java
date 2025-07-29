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

    // You can also add registerUser() and other methods here
}
