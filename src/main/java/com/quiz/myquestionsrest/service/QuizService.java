package com.quiz.myquestionsrest.service;

import com.quiz.myquestionsrest.model.Quiz;
import com.quiz.myquestionsrest.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;

    public List<Quiz> findAll() {
        return quizRepository.findAll();
    }

    public Optional<Quiz> findQuizById(int id) {
        return Optional.ofNullable(quizRepository.findById(id));
    }

    public Optional<Quiz> findQuizByTitle(String title) {
        return quizRepository.findByTitle(title);
    }

    public void saveQuiz(Quiz quiz) {
        quizRepository.save(quiz);
    }

    public void deleteById(int id) {
        quizRepository.deleteById(id);
    }

}
