package com.example.todoapp;

import com.example.todoapp.Models.Task;
import com.example.todoapp.Models.ToDoList;
import com.example.todoapp.Models.ToDoUser;
import com.example.todoapp.Repositories.TaskRepository;
import com.example.todoapp.Repositories.ToDoListRepository;
import com.example.todoapp.Repositories.ToDoUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class ToDoAppApplication implements CommandLineRunner {

    private final ToDoListRepository toDoListRepository;
    private final ToDoUserRepository toDoUserRepository;
    private final TaskRepository taskRepository;

    public static void main(String[] args) {
        SpringApplication.run(ToDoAppApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void run(String... args) throws Exception {
        //setUp
        ToDoUser toDoUser = new ToDoUser("Adam", "password", "adam@mail.cz",  "ROLE_USER");
        ToDoList toDoList = new ToDoList();
        Task task = new Task("Buy milk", "Buy milk when you will go to work tomorrow morning");
        toDoListRepository.save(toDoList);
        toDoUserRepository.save(toDoUser);
        taskRepository.save(task);

        toDoUser.setToDoList(toDoList);
        toDoList.setToDoUser(toDoUser);
        toDoList.setListOfTasks(List.of(task));
        task.setToDoList(toDoList);
        toDoListRepository.save(toDoList);
        toDoUserRepository.save(toDoUser);
        taskRepository.save(task);

    }
}
