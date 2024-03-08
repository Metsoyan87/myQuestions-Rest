package com.quiz.myquestionsrest.service;

import com.quiz.myquestionsrest.model.Answer;
import com.quiz.myquestionsrest.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;



    public Optional<Answer> findByAnswerById(int id) {
        return answerRepository.findById(id);

    }


    public void saveAnswer(Answer answer)  {

    }


    public void deleteAnswerById(int id) {

    }

    public List<Answer> findAll() {
        return answerRepository.findAll();
    }
}
