package com.example.user_service;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	
	User findByEmail(String email);
	
    // Spring Data JPA gives you save(), findById(), deleteById(), etc.
}