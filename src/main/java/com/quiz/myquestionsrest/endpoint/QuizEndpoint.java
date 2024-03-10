package com.quiz.myquestionsrest.endpoint;

import com.quiz.myquestionsrest.dto.UserDto;
import com.quiz.myquestionsrest.model.Quiz;
import com.quiz.myquestionsrest.model.User;
import com.quiz.myquestionsrest.service.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class QuizEndpoint {

    private final QuizService quizService;


    @GetMapping("/quizs")
    public List<Quiz> getAllQuiz() {
        log.info("called by {controller get quiz }");
        List<Quiz> quizList = quizService.findAll();
        if (quizList.isEmpty()) {
            log.info("Quiz not found");
            return (List<Quiz>) ResponseEntity.noContent().build();
        } else {
            log.info("Quiz found, returning quiz list");
            return ResponseEntity.ok(quizList).getBody();
        }
    }

    @DeleteMapping("/quizs/{id}")
    public ResponseEntity deleteQuizById(@PathVariable("id") int id) {
        Optional<Quiz> quizOptional = quizService.findQuizById(id);
        log.info("called by {controller delete quiz by id }");

        if (quizOptional.isPresent()) {
            quizService.deleteById(id);
            return new ResponseEntity<>("Quiz deleted successfully!", HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>("Quiz not found.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/quizs/{id}")
    public ResponseEntity<List<Quiz>> getQuizById(@PathVariable("id") int id) {
        Optional<Quiz> userOptional = quizService.findQuizById(id);
        if (userOptional.isPresent()) {
            List<Quiz> quizList = Collections.singletonList(userOptional.get());
            log.info("Quiz with id {} found", id);
            return ResponseEntity.ok(quizList);
        } else {
            log.info("Quiz with id {} not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
