package com.example.todoapp.Services;

import com.example.todoapp.Models.ToDoUser;

public interface ToDoUserService {

    boolean userOwnsToDoList (String username, Long toDoListId);
    void saveNewUser(ToDoUser toDoUser);

    ToDoUser findByUser(ToDoUser toDoUser);

    boolean checkIfUsernameExists(String username);
}
