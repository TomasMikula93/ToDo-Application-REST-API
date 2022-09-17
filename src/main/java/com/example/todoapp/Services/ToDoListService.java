package com.example.todoapp.Services;

import com.example.todoapp.Models.DTOs.ToDoListDTO;
import com.example.todoapp.Models.Task;

public interface ToDoListService {
    boolean checkSizeOfList(long id);

    ToDoListDTO makeToDoListDTO(long id);

    boolean checkIfListExists(long idOfToDoList);

    void addTaskToList(Task task, long idOfToDoList);

    void markTaskAsDone(Task task);

    void changeTaskName(Task task);
    void changeTaskDescription(Task task);

    void deleteTask(Task task);

    void changeTaskPriority(Task task);

    ToDoListDTO filterTasksByPriority(String value, long id);

    ToDoListDTO filterTasksByStatus(String value, long id);
}
