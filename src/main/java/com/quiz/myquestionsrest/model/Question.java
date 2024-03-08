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
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "varchar(250)")
    private String title;

    @Column(columnDefinition = "int(11)")
    private int score;

    @ManyToOne
    private Quiz quiz;

    @Enumerated(value = EnumType.STRING)
    private QuestionType questionType;



}
