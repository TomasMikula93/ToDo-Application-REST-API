package com.example.todoapp.Controllers;

import com.example.todoapp.Filters.JwtRequestFilter;
import com.example.todoapp.Models.DTOs.ErrorMsgDTO;
import com.example.todoapp.Models.DTOs.MessageDTO;
import com.example.todoapp.Models.DTOs.ToDoListDTO;
import com.example.todoapp.Models.Task;
import com.example.todoapp.Services.ToDoListService;
import com.example.todoapp.Services.ToDoUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TaskController {

    private final ToDoUserService toDoUserService;
    private final ToDoListService toDoListService;


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
    @PostMapping("/task/{idOfToDoList}")
    public ResponseEntity<Object> addTask(@RequestHeader(value = "Authorization") String token, @RequestBody Task task, @PathVariable long idOfToDoList) {
        if (!toDoUserService.userOwnsToDoList(JwtRequestFilter.username, idOfToDoList) || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("This ToDo List does not belong to authenticated player"));
        }
        if (!toDoListService.checkIfListExists(idOfToDoList)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("This ToDo List does not exists"));
        }
        toDoListService.addTaskToList(task, idOfToDoList);
        return ResponseEntity.status(200).
                body(new MessageDTO("Task has been created."));
    }

    @PutMapping("/task/done/{idOfList}")
    public ResponseEntity<Object> markAsDone(@RequestHeader(value = "Authorization") String token, @RequestBody Task task, @PathVariable long idOfList) {
        if (!toDoUserService.userOwnsToDoList(JwtRequestFilter.username, idOfList) || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("This ToDo List does not belong to authenticated player"));
        }

        toDoListService.markTaskAsDone(task);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                body(new MessageDTO("Task is done!"));
    }

    @PutMapping("/task/name/{idOfList}")
    public ResponseEntity<Object> changeTaskName(@RequestHeader(value = "Authorization") String token, @RequestBody Task task, @PathVariable long idOfList) {
        if (!toDoUserService.userOwnsToDoList(JwtRequestFilter.username, idOfList) || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("This ToDo List does not belong to authenticated player"));
        }

        toDoListService.changeTaskName(task);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                body(new MessageDTO("Name of task has been changed!"));
    }

    @PutMapping("/task/description/{idOfList}")
    public ResponseEntity<Object> changeTaskDescription(@RequestHeader(value = "Authorization") String token, @RequestBody Task task, @PathVariable long idOfList) {
        if (!toDoUserService.userOwnsToDoList(JwtRequestFilter.username, idOfList) || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("This ToDo List does not belong to authenticated player"));
        }

        toDoListService.changeTaskDescription(task);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                body(new MessageDTO("Description of task has been changed!"));
    }

    @DeleteMapping("/task/{idOfList}")
    public ResponseEntity<Object> deleteTask(@RequestHeader(value = "Authorization") String token, @RequestBody Task task, @PathVariable long idOfList) {
        if (!toDoUserService.userOwnsToDoList(JwtRequestFilter.username, idOfList) || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("This ToDo List does not belong to authenticated player"));
        }
        toDoListService.deleteTask(task);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                body(new MessageDTO("Task has been deleted!"));

    }

}
