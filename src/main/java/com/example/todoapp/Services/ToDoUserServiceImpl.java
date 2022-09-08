package com.example.todoapp.Services;

import com.example.todoapp.Models.Enums.Roles;
import com.example.todoapp.Models.ToDoList;
import com.example.todoapp.Models.ToDoUser;
import com.example.todoapp.Repositories.ToDoListRepository;
import com.example.todoapp.Repositories.ToDoUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ToDoUserServiceImpl implements ToDoUserService, UserDetailsService {

    private final ToDoUserRepository toDoUserRepository;
    private final ToDoListRepository toDoListRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void saveNewUser(ToDoUser toDoUser) {
        String encodedPassword = passwordEncoder.encode(toDoUser.getPassword());
        ToDoList toDoList = new ToDoList();
        toDoListRepository.save(toDoList);
        ToDoUser newToDoUser = new ToDoUser(toDoUser.getUsername(), encodedPassword, Roles.USER.getRole());
        toDoUserRepository.save(newToDoUser);
        newToDoUser.setToDoList(toDoList);
        toDoList.setToDoUser(newToDoUser);
        toDoUserRepository.save(newToDoUser);
        toDoListRepository.save(toDoList);

    }

    @Override
    public ToDoUser findByUser(ToDoUser toDoUser) {
        return toDoUserRepository.findByUsername(toDoUser.getUsername());
    }

    @Override
    public boolean checkIfUsernameExists(String username) {
        return toDoUserRepository.findOptionalByUsername(username).isPresent();
    }

    @Override
    public boolean userOwnsToDoList(String username, Long toDoListId) {
        return toDoListRepository.findByToDoUserUsernameAndAndId(username, toDoListId).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ToDoUser toDoUser = toDoUserRepository.findByUsername(username);
        if (toDoUser == null) {
            log.error("Player not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("Player found in the database: {}", username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(toDoUser.getRole()));
        return new User(toDoUser.getUsername(), toDoUser.getPassword(), authorities);
    }
}
