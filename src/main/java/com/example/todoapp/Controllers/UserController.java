package com.example.todoapp.Controllers;

import com.example.todoapp.Models.DTOs.ErrorMsgDTO;
import com.example.todoapp.Models.DTOs.MessageDTO;
import com.example.todoapp.Models.DTOs.UserDTO;
import com.example.todoapp.Models.ToDoUser;
import com.example.todoapp.Registration.ConfirmationTokenService;
import com.example.todoapp.Services.ToDoUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final ToDoUserService toDoUserService;
    private final ConfirmationTokenService confirmationTokenService;


    @PostMapping("/registration")
    public ResponseEntity<Object> registration(@RequestBody ToDoUser toDoUser) {
        if (toDoUserService.checkIfUsernameExists(toDoUser.getUsername())) {
            return ResponseEntity.status(400).body(new ErrorMsgDTO("This username already exists!"));
        }
        if (toDoUserService.checkIfEmailExists(toDoUser.getEmail())) {
            return ResponseEntity.status(400).body(new ErrorMsgDTO("This email already exists!"));
        }
        if (toDoUser.getUsername().isEmpty() || toDoUser.getUsername().isBlank() || toDoUser.getUsername() == null) {
            return ResponseEntity.status(400).body(new ErrorMsgDTO("Incorrect username."));
        }
        if (toDoUser.getPassword().isEmpty() || toDoUser.getPassword().isBlank() || toDoUser.getPassword() == null
                || toDoUser.getPassword().length() < 8) {
            return ResponseEntity.status(400).body(new ErrorMsgDTO("Incorrect password."));
        }

        if (!toDoUserService.emailIsValidate(toDoUser.getEmail())) {
            return ResponseEntity.status(400).body(new ErrorMsgDTO("Email is not valid"));

        }
        toDoUserService.saveNewUser(toDoUser);
        return ResponseEntity.status(200).body(new UserDTO(toDoUserService.findByUser(toDoUser).getId(), toDoUser.getUsername()));
    }

    @GetMapping(path = "registration/confirm")
    public ResponseEntity<Object> confirm(@RequestParam("token") String token) {
        if (confirmationTokenService
                .getTokenOptional(token).isEmpty()) {
            return ResponseEntity.status(400).body(new ErrorMsgDTO("Token was not found!"));
        }
        if (confirmationTokenService.findToken(token).getConfirmedAt() != null) {
            return ResponseEntity.status(400).body(new ErrorMsgDTO("Account is already confirmed"));
        }
        if (confirmationTokenService.findToken(token).getExpiresAt().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(400).body(new ErrorMsgDTO("Token is expired"));
        }
        confirmationTokenService.setConfirmedAt(token);
        toDoUserService.enableAppUser(
                confirmationTokenService.findToken(token).getToDoUser().getUsername());
        return ResponseEntity.status(200).body(new MessageDTO("Thank you, your account is activated!"));
    }

    @GetMapping("registration/newToken")
    public ResponseEntity<Object> generateNewConfirmToken(@RequestBody ToDoUser toDoUser) {
        if (!toDoUserService.checkIfUsernameExists(toDoUser.getUsername())) {
            return ResponseEntity.status(400).body(new ErrorMsgDTO("This username doesn't exist!"));
        }
        if (!toDoUserService.emailIsValidate(toDoUser.getEmail())) {
            return ResponseEntity.status(400).body(new ErrorMsgDTO("Email is not valid"));
        }
        if(!toDoUserService.emailMatches(toDoUser.getEmail(), toDoUser.getUsername())){
            return ResponseEntity.status(400).body(new ErrorMsgDTO("Incorrect email for this username"));
        }
        if (toDoUserService.userAccountIsEnabled(toDoUser.getUsername())) {
            return ResponseEntity.status(400).body(new ErrorMsgDTO("Account is already activated"));
        }

        toDoUserService.generateNewToken(toDoUser.getUsername(), toDoUser.getEmail());
        return ResponseEntity.status(200).body(new MessageDTO("Your new confirmation token has been generated!"));
    }

    // just for testing React front-end s
    @GetMapping("/user")
    public ResponseEntity<Object> getUsers() {
        return ResponseEntity.status(200).body(toDoUserService.makeListOfUsersDTO());
    }

}
