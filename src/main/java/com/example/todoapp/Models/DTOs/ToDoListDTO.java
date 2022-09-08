package com.example.todoapp.Models.DTOs;

import com.example.todoapp.Models.Task;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ToDoListDTO {
    private List<TaskDTO> listOfTask;
}
