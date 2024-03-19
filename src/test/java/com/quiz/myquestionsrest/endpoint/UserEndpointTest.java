package com.quiz.myquestionsrest.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quiz.myquestionsrest.dto.CreateUserDto;
import com.quiz.myquestionsrest.dto.EditUserDto;
import com.quiz.myquestionsrest.dto.UserAuthDto;
import com.quiz.myquestionsrest.dto.UserDto;
import com.quiz.myquestionsrest.mapper.UserMapper;
import com.quiz.myquestionsrest.model.User;
import com.quiz.myquestionsrest.model.UserType;
import com.quiz.myquestionsrest.repository.UserRepository;
import com.quiz.myquestionsrest.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserEndpointTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private UserMapper userMapper;
    private static final String BASE_URL = "http://localhost:8080";


    @Test
    public void getUsers_ReturnsListOfUsers() throws Exception {
        List<User> mockUsers = new ArrayList<>();
        mockUsers.add(new User("Gago", "Pijo"));
        mockUsers.add(new User("Bulo", "Pulo"));

        List<UserDto> mockUserDtos = new ArrayList<>();
        mockUserDtos.add(new UserDto("Gago", "Pijo"));
        mockUserDtos.add(new UserDto("Bulo", "Pulo"));

        when(userService.findAllUsers()).thenReturn(mockUsers);

        when(userMapper.map(mockUsers)).thenReturn(mockUserDtos);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }


    @Test
    public void getUserById_UserExists_ReturnsUserDto() throws Exception {
        // Arrange
        int userId = 1;
        User user = new User(userId, "John Doe");
        UserDto userDto = new UserDto(userId, "John Doe");

        given(userService.findUserById(userId)).willReturn(Optional.of(user));
        given(userMapper.map(user)).willReturn(userDto);

        // Act & Assert
        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    public void testDelete_User_By_Id() throws Exception {
        int userId = 1;

        // Mocking userService behavior
        when(userService.findUserById(userId)).thenReturn(Optional.of(new User(userId, "TestUser")));

        // Perform the DELETE request
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Verify that userService.deleteById was called
        verify(userService, times(1)).deleteById(userId);
    }

    @Test
    public void deleteUserById_shouldReturnNotFound_whenUserDoesNotExist() throws Exception {
        // Given
        int nonExistentUserId = 1000; // Assuming user with ID 1000 doesn't exist

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", nonExistentUserId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getUser_ByIdNot_Found() throws Exception {

        User user = User.builder()
                .id(250)
                .name("test")
                .surname("tester")
                .email("test@mail.ru")
                .password("1111")
                .userType(UserType.STUDENT)
                .build();
        userRepository.save(user);
        ResultActions response = mockMvc.perform(get(BASE_URL + "/users/" + user.getId()));
        response.andExpect(status().isNotFound());

    }

    @Test
    public void testRegisterUser_Success() throws Exception {
        // Mocking the behavior of UserService
        when(userService.findByEmail(anyString())).thenReturn(Optional.empty());

        // Mocking the behavior of UserMapper
        when(userMapper.map(any(CreateUserDto.class))).thenReturn(new User());

        // Prepare request body
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setEmail("test@example.com");
        createUserDto.setPassword("password");

        // Perform POST request
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createUserDto)))
                .andExpect(status().isOk());

        // Verify that userService.save() is called
        verify(userService, times(1)).save(any(User.class));
    }

    @Test
    public void testRegisterUser_Conflict() throws Exception {
        // Mocking the behavior of UserService
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        // Prepare request body
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setEmail("test@example.com");
        createUserDto.setPassword("password");

        // Perform POST request
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createUserDto)))
                .andExpect(status().isConflict());
    }

    // Utility method to convert object to JSON string
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void auth_WithValidCredentials_ReturnsAuthToken() throws Exception {
        // Given
        String email = "test@example.com";
        String password = "password";
        UserAuthDto userAuthDto = new UserAuthDto(email, password);
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        when(userMapper.map(user)).thenReturn(new UserDto(user.getId(), user.getEmail()));

        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/user/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAuthDto)))
                // Then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.email").value(email));
    }

    @Test
    public void auth_WithInvalidCredentials_ReturnsUnauthorized() throws Exception {
        // Given
        String email = "test@example.com";
        String password = "password";
        UserAuthDto userAuthDto = new UserAuthDto(email, password);
        when(userService.findByEmail(email)).thenReturn(Optional.empty());

        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/user/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAuthDto)))
                // Then
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void updateUser_ReturnsOk() throws Exception {
        EditUserDto editUserDto = new EditUserDto();
        editUserDto.setEmail("UpdatedName");
        editUserDto.setEmail("updated@example.com");

        doNothing().when(userService).editUser(anyInt(), any(EditUserDto.class));

        mockMvc.perform(put("/users/edit/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editUserDto)))
                .andExpect(status().isOk());
    }
}