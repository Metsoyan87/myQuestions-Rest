package com.quiz.myquestionsrest.service;

import com.quiz.myquestionsrest.exception.EntityNotFoundException;
import com.quiz.myquestionsrest.model.Quiz;
import com.quiz.myquestionsrest.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;

    public List<Quiz> findAll() {
        return quizRepository.findAll();
    }

    public Optional<Quiz> findQuizById(int id) {
        return quizRepository.findById(id);
    }

    public Optional<Quiz> findQuizByTitle(String title) {
        return quizRepository.findByTitle(title);
    }

    public Quiz saveQuiz(Quiz quiz) throws EntityNotFoundException {
        if (quiz == null) {
            throw new EntityNotFoundException("User null");
        }
        return quizRepository.save(quiz);
    }

    public void deleteById(int id) {
        if (quizRepository.findById(id).isEmpty()) {
            log.info("Quiz dos not exist");
            throw new RuntimeException("Quiz can not a found");
        }
        quizRepository.deleteById(id);
    }

}
