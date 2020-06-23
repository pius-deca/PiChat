package com.github.pius.pichats.service;

public interface EmailSenderService {
    void sendMail(String email, String subject, String text);
}
