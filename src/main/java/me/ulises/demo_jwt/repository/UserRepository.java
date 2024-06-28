package me.ulises.demo_jwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import me.ulises.demo_jwt.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{
    Optional<User> findByUsername(String username);
}
