package com.example.todoapp.Models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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
    private boolean isDone;

    @ManyToOne
    @JoinColumn(name = "toDoList")
    private ToDoList toDoList;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.isDone = false;
    }
}
