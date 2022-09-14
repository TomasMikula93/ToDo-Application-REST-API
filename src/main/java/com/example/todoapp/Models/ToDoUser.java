package com.example.todoapp.Models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ToDoUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    private String email;
    private String role;

    @OneToOne(mappedBy = "toDoUser", cascade = CascadeType.ALL)
    private ToDoList toDoList;

    public ToDoUser(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;

    }

    public ToDoUser(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }
}
