package com.quiz.myquestionsrest.repository;

import com.quiz.myquestionsrest.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    Optional<Question> findByTitle(String title);


}
