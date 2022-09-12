package com.example.todoapp.Controllers;

import com.example.todoapp.Filters.JwtRequestFilter;
import com.example.todoapp.Models.DTOs.ErrorMsgDTO;
import com.example.todoapp.Models.DTOs.ToDoListDTO;
import com.example.todoapp.Models.DTOs.UserDTO;
import com.example.todoapp.Models.Task;
import com.example.todoapp.Models.ToDoUser;
import com.example.todoapp.Services.ToDoListService;
import com.example.todoapp.Services.ToDoUserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final ToDoUserService toDoUserService;


    @PostMapping("/registration")
    public ResponseEntity<Object> registration(@RequestBody ToDoUser toDoUser) {
        if (toDoUserService.checkIfUsernameExists(toDoUser.getUsername())) {
            return ResponseEntity.status(400).body(new ErrorMsgDTO("This username already exists!"));
        }
        toDoUserService.saveNewUser(toDoUser);
        return ResponseEntity.status(200).body(new UserDTO(toDoUserService.findByUser(toDoUser).getId(), toDoUser.getUsername()));
    }


    // just for testing React front-end s
    @GetMapping("/user")
    public ResponseEntity<Object> getUsers(){
        return ResponseEntity.status(200).body(toDoUserService.makeListOfUsersDTO());
    }

}
