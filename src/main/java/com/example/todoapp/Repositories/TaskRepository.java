package com.example.todoapp.Repositories;

import com.example.todoapp.Models.Task;
import com.example.todoapp.Models.ToDoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Task findById(long id);
    Optional<Task> findOptionalById(long id);

}
