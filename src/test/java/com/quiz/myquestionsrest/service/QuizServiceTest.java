package com.quiz.myquestionsrest.service;

import com.quiz.myquestionsrest.exception.EntityNotFoundException;
import com.quiz.myquestionsrest.model.Quiz;
import com.quiz.myquestionsrest.repository.QuizRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class QuizServiceTest {


    @MockBean
    private QuizRepository quizRepository;

    @Autowired
    QuizService quizService;

    @Test
    public void testFindAll() {
        Quiz quiz = Quiz.builder()
                .id(1)
                .title("Gago")
                .created_date_time(new Date())
                .build();

        Quiz quiz1 = Quiz.builder()
                .id(2)
                .title("Gago")
                .created_date_time(new Date())
                .build();
        List<Quiz> quizList = Arrays.asList(quiz1, quiz);
        when(quizRepository.findAll()).thenReturn(quizList);

        List<Quiz> result = quizService.findAll();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(quiz1));
        assertTrue(result.contains(quiz));
    }

    @Test
    public void testFindQuizById_WhenQuizExists() {
        // Arrange
        int id = 1;
        Quiz quiz = new Quiz();
        when(quizRepository.findById(id)).thenReturn(Optional.of(quiz));
        Optional<Quiz> result = quizService.findQuizById(id);
        assertTrue(result.isPresent());
        assertEquals(quiz, result.get());
    }

    @Test
    public void testFindQuizById_WhenQuizDoesNotExist() {
        // Arrange
        int id = 2;
        when(quizRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Quiz> result = quizService.findQuizById(id);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findQuizByTitle() {

        String title = "Gugo";
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        when(quizRepository.findByTitle(title)).thenReturn(Optional.of(quiz));

        Optional<Quiz> result = quizService.findQuizByTitle(title);
        assertEquals(Optional.of(quiz), result);
        verify(quizRepository, times(1)).findByTitle(title);

    }

    @Test
    public void testFindByTitle_WhenTitleDoesNotExist() {
        String title = "Gogu";
        when(quizRepository.findByTitle(title)).thenReturn(Optional.empty());
        Optional<Quiz> result = quizService.findQuizByTitle(title);
        verify(quizRepository, times(1)).findByTitle(title);
    }

    @Test
    void saveQuiz() throws EntityNotFoundException {
        Quiz quiz = Quiz.builder()
                .id(1)
                .title("Gago")
                .created_date_time(new Date())
                .build();
        when(quizRepository.save(any())).thenReturn(quiz);

        quizService.saveQuiz(Quiz.builder()
                .id(1)
                .title("Gago")
                .created_date_time(new Date())
                .build());
        verify(quizRepository, times(1)).save(any());
        Quiz savedQuiz = quizService.saveQuiz(quiz);
        assertNotNull(savedQuiz);
        assertEquals(quiz, savedQuiz);

    }

    @Test
    void saveNull() throws EntityNotFoundException {
        Quiz quiz = Quiz.builder()
                .id(1)
                .title("Gago")
                .created_date_time(new Date())
                .build();
        when(quizRepository.save(any())).thenReturn(quiz);

        assertThrows(EntityNotFoundException.class, () -> {
            quizService.saveQuiz(null);
        });

        verify(quizRepository, times(0)).save(any());
        quizService.saveQuiz(Quiz.builder()
                .id(1)
                .title("Gago")
                .created_date_time(new Date())
                .build());
        verify(quizRepository, times(1)).save(any());

    }

    @Test
    void deleteById() {

        int quizId = 1;
        when(quizRepository.findById(quizId)).thenReturn(Optional.of(new Quiz()));
        quizService.deleteById(quizId);
        verify(quizRepository, times(1)).deleteById(quizId);

    }
    @Test
    void deleteById_QuizDoesNotExist_ExceptionThrown() {
        int quizId = 1;
        when(quizRepository.findById(quizId)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> quizService.deleteById(quizId));
        assertEquals("Quiz can not a found", exception.getMessage());
        verify(quizRepository, times(1)).findById(quizId);
        verify(quizRepository, never()).deleteById(quizId);
    }
}