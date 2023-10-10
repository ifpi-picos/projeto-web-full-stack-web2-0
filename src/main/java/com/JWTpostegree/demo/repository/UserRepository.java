package com.JWTpostegree.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.JWTpostegree.demo.model.Users;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);
}

