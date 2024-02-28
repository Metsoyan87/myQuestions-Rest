package com.quiz.myquestionsrest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditUserDto {

    private int id;
    private String email;
    private String phone;
    private String password;
    private String driverLicense;
    private String card;
    private String image;

}
