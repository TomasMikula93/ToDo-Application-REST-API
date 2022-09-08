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

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final ToDoUserService toDoUserService;
    private final ToDoListService toDoListService;


    @PostMapping("/registration")
    public ResponseEntity<Object> registration(@RequestBody ToDoUser toDoUser) {
        if (toDoUserService.checkIfUsernameExists(toDoUser.getUsername())) {
            return ResponseEntity.status(400).body(new ErrorMsgDTO("This username already exists!"));
        }
        toDoUserService.saveNewUser(toDoUser);
        return ResponseEntity.status(200).body(new UserDTO(toDoUserService.findByUser(toDoUser).getId(), toDoUser.getUsername()));
    }

    @GetMapping("/list/{idOfToDoList}")
    public ResponseEntity<Object> toDoList(@RequestHeader(value = "Authorization") String token, @PathVariable long idOfToDoList) {
        if (!toDoUserService.userOwnsToDoList(JwtRequestFilter.username, idOfToDoList) || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("This ToDo List does not belong to authenticated player"));
        }
        if (toDoListService.checkSizeOfList(idOfToDoList)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("Your ToDo list is empty!"));
        }
        ToDoListDTO toDoListDTO = toDoListService.makeToDoListDTO(idOfToDoList);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                body(toDoListDTO);
    }


}
