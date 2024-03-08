package com.quiz.myquestionsrest.endpoint;

import com.quiz.myquestionsrest.dto.CreateUserDto;
import com.quiz.myquestionsrest.model.Answer;
import com.quiz.myquestionsrest.model.User;
import com.quiz.myquestionsrest.model.UserType;
import com.quiz.myquestionsrest.service.AnswerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j

public class AnswerEndpoint {

    private final AnswerService answerService;


    @GetMapping("/answers")
    public List<Answer> getAllAnswers() {
        log.info("called by {controller get users }");
        List<Answer> answers = answerService.findAll();
        if (answers.isEmpty()) {
            log.info("answers not found");
            return (List<Answer>) ResponseEntity.noContent().build();
        } else {
            log.info("Answer found, returning answers list");
            return ResponseEntity.ok(answers).getBody();
        }
    }

    @GetMapping("/answers/{id}")
    public ResponseEntity<Answer> getAnswerById(@PathVariable("id") int id) {
        Optional<Answer> answerOptional = answerService.findByAnswerById(id);
        if (answerOptional.isPresent()) {
            Answer answer = answerOptional.get();
            log.info("Answer with id {} found", id);
            return ResponseEntity.ok(answer);
        } else {
            log.info("Answer with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

}
