package com.example.todoapp.Repositories;

import com.example.todoapp.Models.ToDoUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ToDoUserRepository extends JpaRepository<ToDoUser, Long> {
    ToDoUser findByUsername(String username);
    Optional<ToDoUser> findOptionalByUsername(String username);
}
