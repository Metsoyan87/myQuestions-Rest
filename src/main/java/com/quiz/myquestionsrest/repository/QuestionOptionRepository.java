package com.quiz.myquestionsrest.repository;

import com.quiz.myquestionsrest.model.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionOptionRepository extends JpaRepository<QuestionOption, Integer> {

    Optional<QuestionOption> findByTitle(String title);

}
