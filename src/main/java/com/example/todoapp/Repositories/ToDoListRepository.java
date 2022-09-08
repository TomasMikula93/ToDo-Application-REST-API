package com.example.todoapp.Repositories;

import com.example.todoapp.Models.ToDoList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ToDoListRepository extends JpaRepository<ToDoList, Long> {
    Optional<ToDoList> findByToDoUserUsernameAndAndId(String username, long id);
    Optional<ToDoList> findOptionalById(long id);
    ToDoList findById(long id);
}
