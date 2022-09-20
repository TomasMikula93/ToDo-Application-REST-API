package com.example.todoapp.Controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void registration() throws Exception {
        mockMvc.perform(post("/api/registration")// perform action inside POST,GET,DELETE + url. Parameters or path variable can be specified inside. It is string so ""+variable+"" can be used.
                        .content("""
                                {
                                    "username": "Thomas",
                                    "password": "password123",
                                    "email" : "tomas.mikula@centrum.cz"
                                }""")
                        .contentType("application/json"))//you need to specify what type of content is inside of body
                .andExpect(status().is(200));

    }

    @Test
    void getUsers() throws Exception {
        mockMvc.perform(post("/api/registration")// perform action inside POST,GET,DELETE + url. Parameters or path variable can be specified inside. It is string so ""+variable+"" can be used.
                        .content("""
                                {
                                    "username": "Tom",
                                    "password": "password123",
                                    "email" : "t.mikula@centrum.cz"
                                }""")
                        .contentType("application/json"))//you need to specify what type of content is inside of body
                .andExpect(status().is(200));


        mockMvc.perform(get("/api/user"))
          .andExpect(status().is(200));

    }
}