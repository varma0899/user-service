package com.example.user_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.userservice.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;

    // Create user
    @PostMapping(path = "/register" )
    public User createUser(@RequestBody User user) {
    	System.out.println(user);
        return userRepository.save(user);
    }

    // Read all users
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Read user by ID
//    @GetMapping("/{id}")
//    public Optional<User> getUserById(@PathVariable Long id) {
//        return userRepository.findById(id);
//    }

    // Update user
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User user = userRepository.findById(id).orElseThrow();
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());
        return userRepository.save(user);
    }

    // Delete user
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
    
 // Login and return JWT token
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User existingUser = userService.loginUser(user.getEmail(), user.getPassword());

        if (existingUser != null) {
            String token = jwtUtil.generateToken(existingUser.getEmail());
            return ResponseEntity.ok().body("{\"token\": \"" + token + "\"}");
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id, HttpServletRequest request) {
        String token = extractToken(request);
        if (token == null || !jwtUtil.validate(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String usernameFromToken = jwtUtil.getUsername(token);
        User tokenUser = userService.findByUsername(usernameFromToken);
        if (!tokenUser.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        User user = userService.findById(id);
        System.out.println("User validated");
        return ResponseEntity.ok(user);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }


}