package com.quiz.myquestionsrest.repository;

import com.quiz.myquestionsrest.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Integer> {


    Optional<Quiz> findByTitle(String title);




}
