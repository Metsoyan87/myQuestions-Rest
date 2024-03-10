package com.quiz.myquestionsrest.service;

import com.quiz.myquestionsrest.model.Question;
import com.quiz.myquestionsrest.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;



    public Optional<Question> findByTitle(String title) {
        return questionRepository.findByTitle(title);
    }



    public void saveQuestion(Question question)  {
        questionRepository.save(question);
    }

    public Question findById(int id) {
        return questionRepository.findById(id).orElse(null);
    }


    public List<Question> findAll() {
        return questionRepository.findAll();
    }


}
