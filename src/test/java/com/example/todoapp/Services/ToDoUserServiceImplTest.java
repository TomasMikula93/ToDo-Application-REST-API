//package com.example.todoapp.Services;
//
//import com.example.todoapp.Models.ToDoUser;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.mockito.Mockito;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class ToDoUserServiceImplTest {
//
//    private final List<ToDoUser> listOfUsers = new ArrayList<>();
//
//    @BeforeEach
//    void setUp() {
//        ToDoUser toDoUser = new ToDoUser("User1", "password", "ROLE_USER");
//        listOfUsers.add(toDoUser);
//    }
//
//    @Test
//    void testFindAllUsers() {
//
//        ToDoUserService toDoUserService = Mockito.mock(ToDoUserService.class);//mocked class using Mockito
//        Mockito.when(toDoUserService.findAllUsers()).thenReturn(listOfUsers);
//
//        assertTrue(toDoUserService.findAllUsers().size() > 0);
//    }
//}