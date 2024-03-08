package com.quiz.myquestionsrest.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "varchar(50)")
    private String name;

    @Column(columnDefinition = "varchar(50)")
    private String surname;

    @Column(columnDefinition = "varchar(50)")
    private String email;

    @Column(columnDefinition = "varchar(50)")
    private String password;

    @Enumerated(value = EnumType.STRING)
    private UserType userType;

}
