package com.example.hotelapp.Service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class MailSender implements EmailService{
    private final JavaMailSender mailSender;

    public MailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    @Override
    public void sendEmail(String Email, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(Email);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }
}
