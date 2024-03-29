package com.quiz.myquestionsrest.endpoint;

import com.quiz.myquestionsrest.dto.*;
import com.quiz.myquestionsrest.exception.EntityNotFoundException;
import com.quiz.myquestionsrest.mapper.UserMapper;
import com.quiz.myquestionsrest.model.User;
import com.quiz.myquestionsrest.model.UserType;
import com.quiz.myquestionsrest.service.UserService;
import com.quiz.myquestionsrest.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserEndpoint {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;


    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getUsers() {
        log.info("called by {controller get users }");
        List<User> users = userService.findAllUsers();
        if (users.isEmpty()) {
            log.info("Users not found");
            return ResponseEntity.noContent().build();
        } else {
            log.info("Users found, returning user list");
            return ResponseEntity.ok(userMapper.map(users));
        }
    }


    @DeleteMapping("/users/{id}")
    public ResponseEntity deleteById(@PathVariable("id") int id) {
        Optional<User> userOptional = userService.findUserById(id);
        log.info("called by {controller delete user by id }");
        if (userOptional.isPresent()) {
            userService.deleteById(id);
            return new ResponseEntity<>("User deleted successfully!", HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<List<UserDto>> getUserById(@PathVariable("id") int id) {
        Optional<User> userOptional = userService.findUserById(id);
        if (userOptional.isPresent()) {
            List<UserDto> userDtoList = Collections.singletonList(userMapper.map(userOptional.get()));
            log.info("User with id {} found", id);
            return ResponseEntity.ok(userDtoList);
        } else {
            log.info("User with id {} not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @PostMapping("/user")
    public ResponseEntity<?> register(@RequestBody CreateUserDto createUserDto) throws MessagingException, EntityNotFoundException {
        Optional<User> existingUser = userService.findByEmail(createUserDto.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        User user = userMapper.map(createUserDto);
        user.setUserType(UserType.STUDENT);
        user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        return ResponseEntity.ok(userMapper.map(userService.save(user)));
    }

    @PostMapping("/user/auth")
    public ResponseEntity<?> auth(@RequestBody UserAuthDto userAuthDto) {
        Optional<User> byEmail = userService.findByEmail(userAuthDto.getEmail());
        if (byEmail.isPresent()) {
            User user = byEmail.get();
            if (passwordEncoder.matches(userAuthDto.getPassword(), user.getPassword())) {
                log.info("User with username {} get auth token", user.getEmail());

                return ResponseEntity.ok(UserAuthResponseDto.builder()
                        .token(jwtTokenUtil.refreshToken(user.getEmail()))
                        .token(jwtTokenUtil.generateToken(user.getEmail()))
                        .user(userMapper.map(user))
                        .build()
                );
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PutMapping("/users/edit/{id}")
    public EditUserDto updateUser(@PathVariable int id,
                                  @RequestBody EditUserDto editUserDto) {
        userService.editUser(id, editUserDto);
        log.info("user update");
        return editUserDto;
    }

}





