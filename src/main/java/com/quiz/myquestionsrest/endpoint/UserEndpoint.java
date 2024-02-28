package com.quiz.myquestionsrest.endpoint;


import com.quiz.myquestionsrest.dto.*;
import com.quiz.myquestionsrest.mapper.UserMapper;
import com.quiz.myquestionsrest.model.User;
import com.quiz.myquestionsrest.model.UserType;
import com.quiz.myquestionsrest.repository.UserRepository;
import com.quiz.myquestionsrest.security.CurrentUser;
import com.quiz.myquestionsrest.service.UserService;
import com.quiz.myquestionsrest.util.JwtTokenUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
    public List<UserDto> getUsers(@AuthenticationPrincipal CurrentUser currentUser) {
        log.info("called by {controller get users }");
        return userMapper.map(userService.getAllUser());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity deleteById(@PathVariable("id") int id) {
        log.info("called by {controller delete user by id }");
        userService.deleteById(id);
        return new ResponseEntity<>("Users deleted successfully!.", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") int id) {
        log.info("called by {controller user get by id }");
        return userService.findUserById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/user")
    public ResponseEntity<?> register(@RequestBody CreateUserDto createUserDto) throws MessagingException {
        Optional<User> existingUser = userService.findByEmail(createUserDto.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        User user = userMapper.map(createUserDto);
        user.setUserType(UserType.STUDENT);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
                        .token(jwtTokenUtil.generateToken(user.getEmail()))
                        .user(userMapper.map(user))
                        .build()
                );
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/users/edit/{id}")
    public EditUserDto updateUser(@PathVariable int id,
                                  @RequestBody EditUserDto editUserDto) {
        userService.editUser(id, editUserDto);
        log.info("called by update {controller users }");
        return editUserDto;
    }
}
