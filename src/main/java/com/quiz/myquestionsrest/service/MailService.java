package com.quiz.myquestionsrest.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    @Async
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);

        mailSender.send(mailMessage);
    }
    @Async
    public void sendHtmlEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);
        mailSender.send(mimeMessage);
    }

}