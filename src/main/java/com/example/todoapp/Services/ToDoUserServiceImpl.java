package com.example.todoapp.Services;

import com.example.todoapp.Models.DTOs.UserDTO;
import com.example.todoapp.Models.Enums.Roles;
import com.example.todoapp.Models.ToDoList;
import com.example.todoapp.Models.ToDoUser;
import com.example.todoapp.Registration.*;
import com.example.todoapp.Repositories.ToDoListRepository;
import com.example.todoapp.Repositories.ToDoUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ToDoUserServiceImpl implements ToDoUserService, UserDetailsService {

    private final ToDoUserRepository toDoUserRepository;
    private final ToDoListRepository toDoListRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailValidation emailValidation;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;

    @Override
    public void saveNewUser(ToDoUser toDoUser) {
        String encodedPassword = passwordEncoder.encode(toDoUser.getPassword());
        ToDoList toDoList = new ToDoList();
        toDoListRepository.save(toDoList);

        ToDoUser newToDoUser = new ToDoUser(toDoUser.getUsername(),
                encodedPassword,
                toDoUser.getEmail(),
                Roles.USER.getRole());

        toDoUserRepository.save(newToDoUser);
        newToDoUser.setToDoList(toDoList);
        toDoList.setToDoUser(newToDoUser);
        toDoUserRepository.save(newToDoUser);
        toDoListRepository.save(toDoList);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                newToDoUser
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        String link = "http://localhost:8080/api/registration/confirm?token=" + token;
        emailService.send(toDoUser.getEmail(), buildEmail(toDoUser.getUsername(), link));
    }

    @Override
    public void generateNewToken(String username, String email) {
        ToDoUser toDoUser = toDoUserRepository.findByUsername(username);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                toDoUser
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        String link = "http://localhost:8080/api/registration/confirm?token=" + token;
        emailService.send(toDoUser.getEmail(), buildEmail(toDoUser.getUsername(), link));
    }

    @Override
    public ToDoUser findByUser(ToDoUser toDoUser) {
        return toDoUserRepository.findByUsername(toDoUser.getUsername());
    }

    @Override
    public List<ToDoUser> findAllUsers() {
        return toDoUserRepository.findAll();
    }

    @Override
    public List<UserDTO> makeListOfUsersDTO() {
        List<ToDoUser> sublist = findAllUsers();
        List<UserDTO> list = new ArrayList<>();
        for (ToDoUser toDoUser : sublist) {
            list.add(new UserDTO(toDoUser.getId(),
                    toDoUser.getUsername()));
        }
        return list;
    }

    @Override
    public boolean checkIfUsernameExists(String username) {
        return toDoUserRepository.findOptionalByUsername(username).isPresent();
    }
    @Override
    public boolean checkIfEmailExists(String email) {
        return toDoUserRepository.findOptionalByEmail(email).isPresent();
    }

    @Override
    public boolean checkIfTokenExpired(ToDoUser toDoUser) {
        ToDoUser user = toDoUserRepository.findByUsername(toDoUser.getUsername());
        for (int i = 0; i < user.getListOfConfirmationTokens().size(); i++) {
            if(user.getListOfConfirmationTokens().get(i).getConfirmedAt() == null){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean emailIsValidate(String email) {
        return emailValidation.isValidEmail(email);
    }


    @Override
    public void enableAppUser(String username) {
        ToDoUser toDoUser = toDoUserRepository.findByUsername(username);
        toDoUser.setEnabled(true);
        toDoUserRepository.save(toDoUser);
    }

    @Override
    public boolean userAccountIsEnabled(String username) {
        return toDoUserRepository.findByUsername(username).isEnabled();
    }

    @Override
    public boolean emailMatches(String email, String username) {
        ToDoUser toDoUser = toDoUserRepository.findByUsername(username);
        return Objects.equals(toDoUser.getEmail(), email);
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

        return new User(toDoUser.getUsername(), toDoUser.getPassword(), toDoUser.getAuthorities());
    }

    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
}
