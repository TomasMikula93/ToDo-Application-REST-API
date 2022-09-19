package com.example.todoapp.Models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;

    private String priority;
    private boolean isDone;
    private Date createdAt;
    private Date doneAt;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Tag> listOfTags;

    @ManyToOne
    @JoinColumn(name = "toDoList")
    private ToDoList toDoList;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.priority = "normal";
        this.isDone = false;
        this.listOfTags = new ArrayList<>();
        this.createdAt = new Date();
    }

    public Task(String name, String description, String priority) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.isDone = false;
        this.listOfTags = new ArrayList<>();
        this.createdAt = new Date();
    }
}
