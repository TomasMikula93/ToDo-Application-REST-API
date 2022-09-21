package com.example.todoapp.Controllers;

import com.example.todoapp.Filters.JwtRequestFilter;
import com.example.todoapp.Models.DTOs.ErrorMsgDTO;
import com.example.todoapp.Models.DTOs.MessageDTO;
import com.example.todoapp.Models.DTOs.ToDoListDTO;
import com.example.todoapp.Models.Tag;
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
    public ResponseEntity<Object> toDoList(@RequestHeader(value = "Authorization") String token,
                                           @PathVariable long idOfToDoList) {
        if (!toDoUserService.userOwnsToDoList(JwtRequestFilter.username, idOfToDoList) || token.isEmpty()
                || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("This ToDo List does not belong to authenticated player"));
        }
        if (!toDoListService.checkIfListExists(idOfToDoList)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("This ToDo List does not exists"));
        }
        if (toDoListService.checkSizeOfList(idOfToDoList)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("Your ToDo list is empty"));
        }
        ToDoListDTO toDoListDTO = toDoListService.makeToDoListDTO(idOfToDoList);
        return ResponseEntity.status(200).
                body(toDoListDTO);
    }

    @PostMapping("/task/{idOfToDoList}")
    public ResponseEntity<Object> addTask(@RequestHeader(value = "Authorization") String token, @RequestBody Task task,
                                          @PathVariable long idOfToDoList) {
        if (!toDoUserService.userOwnsToDoList(JwtRequestFilter.username, idOfToDoList) || token.isEmpty()
                || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("This ToDo List does not belong to authenticated player"));
        }
        if (!toDoListService.checkIfListExists(idOfToDoList)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("This ToDo List does not exists"));
        }

        if (task.getName().isEmpty() || task.getName().isBlank() || task.getName() == null || task.getDescription().isEmpty()
                || task.getDescription().isBlank() || task.getDescription() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("Incorrect name or description"));
        }
        toDoListService.addTaskToList(task, idOfToDoList);
        return ResponseEntity.status(200).
                body(new MessageDTO("Task has been created"));
    }

    @PutMapping("/task/done/{idOfList}")
    public ResponseEntity<Object> markAsDone(@RequestHeader(value = "Authorization") String token,
                                             @RequestBody Task task, @PathVariable long idOfList) {
        if (!toDoUserService.userOwnsToDoList(JwtRequestFilter.username, idOfList) || token.isEmpty()
                || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("This ToDo List does not belong to authenticated player"));
        }
        if (!toDoListService.checkIfListExists(idOfList)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("This ToDo List does not exists"));
        }
        if (!toDoListService.checkIfTaskExists(task.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("This task does not exists"));
        }
        toDoListService.markTaskAsDone(task);
        return ResponseEntity.status(200).
                body(new MessageDTO("Task is done"));
    }

    @PutMapping("/task/name/{idOfToDoList}")
    public ResponseEntity<Object> changeTaskName(@RequestHeader(value = "Authorization") String token,
                                                 @RequestBody Task task, @PathVariable long idOfToDoList) {
        if (!toDoUserService.userOwnsToDoList(JwtRequestFilter.username, idOfToDoList) || token.isEmpty()
                || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("This ToDo List does not belong to authenticated player"));
        }
        if (!toDoListService.checkIfListExists(idOfToDoList)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("This ToDo List does not exists"));
        }
        if (!toDoListService.checkIfTaskExists(task.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("This task does not exists"));
        }
        if (task.getName().isEmpty() || task.getName().isBlank() || task.getName() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("Incorrect name"));
        }
        toDoListService.changeTaskName(task);
        return ResponseEntity.status(200).
                body(new MessageDTO("Name of task has been changed!"));
    }

    @PutMapping("/task/description/{idOfToDoList}")
    public ResponseEntity<Object> changeTaskDescription(@RequestHeader(value = "Authorization") String token,
                                                        @RequestBody Task task, @PathVariable long idOfToDoList) {
        if (!toDoUserService.userOwnsToDoList(JwtRequestFilter.username, idOfToDoList) || token.isEmpty()
                || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("This ToDo List does not belong to authenticated player"));
        }
        if (!toDoListService.checkIfListExists(idOfToDoList)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("This ToDo List does not exists"));
        }
        if (!toDoListService.checkIfTaskExists(task.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("This task does not exists"));
        }
        if (task.getDescription().isEmpty() || task.getDescription().isBlank() || task.getDescription() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("Incorrect description"));
        }
        toDoListService.changeTaskDescription(task);
        return ResponseEntity.status(200).
                body(new MessageDTO("Description of task has been changed!"));
    }

    @DeleteMapping("/task/{idOfToDoList}")
    public ResponseEntity<Object> deleteTask(@RequestHeader(value = "Authorization") String token,
                                             @RequestBody Task task, @PathVariable long idOfToDoList) {
        if (!toDoUserService.userOwnsToDoList(JwtRequestFilter.username, idOfToDoList) || token.isEmpty() || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("This ToDo List does not belong to authenticated player"));
        }
        if (!toDoListService.checkIfListExists(idOfToDoList)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("This ToDo List does not exists"));
        }
        if (!toDoListService.checkIfTaskExists(task.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("This task does not exists"));
        }
        toDoListService.deleteTask(task);
        return ResponseEntity.status(200).
                body(new MessageDTO("Task has been deleted!"));

    }

    @PutMapping("/task/priority/{idOfToDoList}")
    public ResponseEntity<Object> changePriority(@RequestHeader(value = "Authorization") String token,
                                                 @RequestBody Task task, @PathVariable long idOfToDoList) {
        if (!toDoUserService.userOwnsToDoList(JwtRequestFilter.username, idOfToDoList) || token.isEmpty()
                || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("This ToDo List does not belong to authenticated player"));
        }
        if (!toDoListService.checkIfListExists(idOfToDoList)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("This ToDo List does not exists"));
        }
        if (!toDoListService.checkIfTaskExists(task.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("This task does not exists"));
        }
        toDoListService.changeTaskPriority(task);
        return ResponseEntity.status(200).
                body(new MessageDTO("Priority of task has been changed!"));
    }

    @PostMapping("/task/tag/{idOfToDoList}/{idOfTask}")
    public ResponseEntity<Object> addTag(@RequestHeader(value = "Authorization") String token, @RequestBody Tag tag,
                                         @PathVariable long idOfToDoList, @PathVariable long idOfTask) {
        if (!toDoUserService.userOwnsToDoList(JwtRequestFilter.username, idOfToDoList) || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("This ToDo List does not belong to authenticated player"));
        }
        if (!toDoListService.checkIfListExists(idOfToDoList)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("This ToDo List does not exists"));
        }

        if (!toDoListService.checkIfTaskExists(idOfTask)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("This task does not exists"));
        }
        if (tag.getName().isBlank() || tag.getName().isEmpty() || tag.getName() == null || !tag.getName().startsWith("#")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("Incorrect tag name"));
        }
        toDoListService.addTagToTask(idOfTask, tag);
        return ResponseEntity.status(200).
                body(new MessageDTO("Tag added"));

    }


    @GetMapping("/task/{idOfToDoList}/filter/priority")
    public ResponseEntity<Object> filterTasksByPriority(@RequestHeader(value = "Authorization") String token,
                                                        @RequestParam String value, @PathVariable long idOfToDoList) {
        if (!toDoUserService.userOwnsToDoList(JwtRequestFilter.username, idOfToDoList) || token.isEmpty() || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("This ToDo List does not belong to authenticated player"));
        }
        if (!toDoListService.checkIfListExists(idOfToDoList)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("This ToDo List does not exists"));
        }
        if (value.isEmpty() || value.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("Incorrect value for filter"));
        }
        return ResponseEntity.status(200).body(toDoListService.filterTasksByPriority(value, idOfToDoList));
    }

    @GetMapping("/task/{idOfToDoList}/filter/status")
    public ResponseEntity<Object> filterTasksByStatus(@RequestHeader(value = "Authorization") String token,
                                                      @RequestParam String value, @PathVariable long idOfToDoList) {
        if (!toDoUserService.userOwnsToDoList(JwtRequestFilter.username, idOfToDoList) || token.isEmpty() || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("This ToDo List does not belong to authenticated player"));
        }
        if (!toDoListService.checkIfListExists(idOfToDoList)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("This ToDo List does not exists"));
        }
        if (value.isEmpty() || value.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("Incorrect value for filter"));
        }

        return ResponseEntity.status(200).body(toDoListService.filterTasksByStatus(value, idOfToDoList));
    }

    @GetMapping("/task/{idOfToDoList}/filter/tag")
    public ResponseEntity<Object> filterTasksByTag(@RequestHeader(value = "Authorization") String token,
                                                   @RequestParam String value, @PathVariable long idOfToDoList) {
        if (!toDoUserService.userOwnsToDoList(JwtRequestFilter.username, idOfToDoList) || token.isEmpty() || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("This ToDo List does not belong to authenticated player"));
        }
        if (!toDoListService.checkIfListExists(idOfToDoList)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ErrorMsgDTO("This ToDo List does not exists"));
        }
//        if (value.isEmpty() || value.isBlank()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
//                    body(new ErrorMsgDTO("Incorrect value for filter"));
//        }
        return ResponseEntity.status(200).body(toDoListService.filterTasksByTag(value, idOfToDoList));
    }

}
