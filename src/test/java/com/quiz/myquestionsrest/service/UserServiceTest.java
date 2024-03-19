package com.quiz.myquestionsrest.service;

import com.quiz.myquestionsrest.dto.EditUserDto;
import com.quiz.myquestionsrest.exception.EntityNotFoundException;
import com.quiz.myquestionsrest.model.User;
import com.quiz.myquestionsrest.model.UserType;
import com.quiz.myquestionsrest.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    UserService userService;

    @Test
    void saveUser() throws EntityNotFoundException {
        User user = User.builder()
                .id(1)
                .name("gago")
                .surname("ispir")
                .email("gago@mail.ru")
                .password("1111")
                .userType(UserType.STUDENT)
                .build();
        when(userRepository.save(any())).thenReturn(user);

        userService.save(User.builder()
                .name("gago")
                .surname("ispir")
                .email("gago@mail.ru")
                .password("1111")
                .userType(UserType.STUDENT)
                .build());
        verify(userRepository, times(1)).save(any());
        User savedUser = userService.save(user);
        assertNotNull(savedUser);
        assertEquals(user, savedUser);

    }

    @Test
    void saveNull() throws EntityNotFoundException {
        User user = User.builder()
                .id(1)
                .name("gago")
                .surname("ispir")
                .email("gago@mail.ru")
                .password("1111")
                .userType(UserType.STUDENT)
                .build();
        when(userRepository.save(any())).thenReturn(user);

        assertThrows(EntityNotFoundException.class, () -> {
            userService.save(null);
        });

        verify(userRepository, times(0)).save(any());

        userService.save(User.builder()
                .name("gago")
                .surname("ispir")
                .email("gago@mail.ru")
                .password("1111")
                .userType(UserType.STUDENT)
                .build());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void getAllUsers() throws EntityNotFoundException {

        User user = User.builder()
                .id(1)
                .name("gago")
                .surname("ispir")
                .email("gago@mail.ru")
                .password("1111")
                .userType(UserType.STUDENT)
                .build();

        User user1 = User.builder()
                .id(2)
                .name("gago")
                .surname("ispir")
                .email("gago@mail.ru")
                .password("1111")
                .userType(UserType.STUDENT)
                .build();

        List<User> userList = Arrays.asList(user, user1);

        when(userRepository.findAll()).thenReturn(userList);
        List<User> result = userService.findAllUsers();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(user1));
        assertTrue(result.contains(user));

    }

    @Test
    void findByEmail() {

        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        Optional<User> result = userService.findByEmail(email);
        assertEquals(Optional.of(user), result);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    public void testFindByEmail_WhenUserDoesNotExist() {
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        Optional<User> result = userService.findByEmail(email);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void findUserById() {
        int userId = 1;
        User user = new User();
        user.setId(userId);
        user.setName("John");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Optional<User> result = userService.findUserById(userId);
        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());
        assertEquals("John", result.get().getName());
    }

    @Test
    public void testFindUserById_WhenUserDoesNotExist() {

        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        Optional<User> result = userService.findUserById(userId);
        assertFalse(result.isPresent());
    }

    @Test
    void deleteById() {
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        assertDoesNotThrow(() -> userService.deleteById(userId));
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void deleteById_UserDoesNotExist_ExceptionThrown() {
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.deleteById(userId));
        assertEquals("User can not a found", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).deleteById(userId);
    }

    @Test
    void editUser() {
        int userId = 1;
        EditUserDto dto = new EditUserDto();
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        assertDoesNotThrow(() -> userService.editUser(userId, dto));
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(user); // Assuming save method is invoked in edit method
    }

    @Test
    void editUser_UserNotFound() {

        int userId = 1;
        EditUserDto dto = new EditUserDto();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> userService.editUser(userId, dto));
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verifyNoMoreInteractions(userRepository);
    }
}