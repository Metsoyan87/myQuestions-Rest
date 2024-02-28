package com.quiz.myquestionsrest.mapper;

import com.quiz.myquestionsrest.dto.CreateUserDto;
import com.quiz.myquestionsrest.dto.UserDto;
import com.quiz.myquestionsrest.model.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper {

//    @Mapping(target = "userType", defaultValue = "STUDENT")
    User map(CreateUserDto createUserDto);

    List<UserDto> map(List<User> users);

    UserDto map(User user);
}
