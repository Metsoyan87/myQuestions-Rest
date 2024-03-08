package com.quiz.myquestionsrest.endpoint;

import com.quiz.myquestionsrest.model.Question;
import com.quiz.myquestionsrest.service.QuestionService;
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
public class QuestionEndpoint {

    private final QuestionService questionService;


    @GetMapping("/questions")
    public List<Question> getQuestions() {
        log.info("called by {controller get question }");
        List<Question> questions = questionService.findAll();
        if (questions.isEmpty()) {
            log.info("Question not found");
            return (List<Question>) ResponseEntity.noContent().build();
        } else {
            log.info("Users found, returning user list");
            return ResponseEntity.ok(questions).getBody();
        }
    }
    @DeleteMapping("/question/{id}")
    public ResponseEntity deleteQuestionById(@PathVariable("id") int id) {
        Optional<Question> questionOptional = Optional.ofNullable(questionService.findById(id));
        log.info("called by {controller delete user by id }");

        if (questionOptional.isPresent()) {
            questionService.deleteById(id);
            return new ResponseEntity<>("Question deleted successfully!", HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>("Question not found.", HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/questions/{id}")
    public ResponseEntity<List<Question>> getQuestionById(@PathVariable("id") int id) {
        Optional<Question> question = Optional.ofNullable(questionService.findById(id));
        if (question.isPresent()) {
            List<Question> questions = Collections.singletonList(question.get());
            log.info("User with id {} found", id);
            return ResponseEntity.ok(questions);
        } else {
            log.info("User with id {} not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
