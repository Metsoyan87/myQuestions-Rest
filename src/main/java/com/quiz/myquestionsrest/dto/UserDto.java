package com.quiz.myquestionsrest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private int id;
    private String name;
    private String surname;
    private String email;


    public UserDto(String number, String john) {
    }

    public UserDto(int userId, String johnDoe) {
    }
}
