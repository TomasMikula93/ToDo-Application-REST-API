package com.example.todoapp.Models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class ToDoList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "toDoUser")
    private ToDoUser toDoUser;

    @OneToMany(mappedBy = "toDoList")
    private List<Task> listOfTasks;

    public ToDoList() {
        this.listOfTasks = new ArrayList<>();
    }
}
