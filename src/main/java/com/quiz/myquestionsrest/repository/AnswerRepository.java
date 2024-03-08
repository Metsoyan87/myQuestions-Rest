package com.quiz.myquestionsrest.repository;


import com.quiz.myquestionsrest.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {

  Answer findAnswerById(int answerId);


}
