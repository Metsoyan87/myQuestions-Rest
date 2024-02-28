package com.quiz.myquestionsrest.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "questionoption")
public class QuestionOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Question question;

    @Column(columnDefinition = "varchar(250)")
    private String title;

    private boolean isCorrect;
}
