package com.quiz.myquestionsrest.service;

import com.quiz.myquestionsrest.model.QuestionOption;
import com.quiz.myquestionsrest.repository.QuestionOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionOptionService {

    private final QuestionOptionRepository questionOptionRepository;


    public Optional<QuestionOption> findByTitle(String title) {
        return questionOptionRepository.findByTitle(title);
    }



    public void saveQuestionOption(QuestionOption questionOption) {
        questionOptionRepository.save(questionOption);

    }


    public List<QuestionOption> findAll() {
        return questionOptionRepository.findAll();
    }


    public Optional<QuestionOption> findQuestionOptionById(int id) {
        return questionOptionRepository.findById(id);

    }
}
