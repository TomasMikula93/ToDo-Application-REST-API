package com.example.todoapp.Services;

import com.example.todoapp.Models.DTOs.UserDTO;
import com.example.todoapp.Models.ToDoUser;

import java.util.List;

public interface ToDoUserService {

    boolean userOwnsToDoList (String username, Long toDoListId);
    void saveNewUser(ToDoUser toDoUser);

    ToDoUser findByUser(ToDoUser toDoUser);
    List<ToDoUser> findAllUsers();
    List<UserDTO> makeListOfUsersDTO();

    boolean checkIfUsernameExists(String username);

    boolean emailIsValidate(String email);

    void enableAppUser(String username);
}
