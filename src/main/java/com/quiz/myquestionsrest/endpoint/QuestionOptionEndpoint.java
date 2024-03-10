package com.quiz.myquestionsrest.endpoint;

import com.quiz.myquestionsrest.model.QuestionOption;
import com.quiz.myquestionsrest.service.QuestionOptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class QuestionOptionEndpoint {

    private final QuestionOptionService questionOptionService;

    @GetMapping("/questionOption")
    public List<QuestionOption> getAllQuestionOption() {
        log.info("called by {controller get QuestionOption }");
        List<QuestionOption> questionOptions = questionOptionService.findAll();
        if (questionOptions.isEmpty()) {
            log.info("QuestionOption not found");
            return (List<QuestionOption>) ResponseEntity.noContent().build();
        } else {
            log.info("QuestionOption found, returning user list");
            return ResponseEntity.ok(questionOptions).getBody();
        }
    }
    @GetMapping("/questionOption/{id}")
    public ResponseEntity<List<QuestionOption>> getQuestionOptionById(@PathVariable("id") int id) {
        Optional<QuestionOption> questionOption = questionOptionService.findQuestionOptionById(id);
        if (questionOption.isPresent()) {
            List<QuestionOption> questionOptionList = Collections.singletonList(questionOption.get());
            log.info("QuestionOption with id {} found", id);
            return ResponseEntity.ok(questionOptionList);
        } else {
            log.info("QuestionOption with id {} not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
