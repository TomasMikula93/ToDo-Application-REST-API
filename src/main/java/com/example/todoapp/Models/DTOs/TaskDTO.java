package com.example.todoapp.Models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TaskDTO {
    private long id;
    private String name;
    private String description;
    private String priority;
    private boolean isDone;
    private List<TagDTO> listOfTags;

    public TaskDTO(long id, String name, String description, String priority, boolean isDone, List<TagDTO> listOfTags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.isDone = isDone;
        this.listOfTags = listOfTags;
    }

    public TaskDTO(long id, String name, String description, String priority, boolean done) {
    }
}
