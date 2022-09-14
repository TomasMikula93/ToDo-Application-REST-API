package com.example.todoapp.Models;

import com.example.todoapp.Registration.ConfirmationToken;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ToDoUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    private String email;
    private String role;
    private boolean enabled;
    private boolean locked;

    @OneToOne(mappedBy = "toDoUser", cascade = CascadeType.ALL)
    private ToDoList toDoList;
    @OneToOne(mappedBy = "toDoUser")
    private ConfirmationToken confirmationToken;


    public ToDoUser(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.enabled = false;
        this.locked = false;

    }

    public ToDoUser(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;


    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
        return Collections.singletonList(authority);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
