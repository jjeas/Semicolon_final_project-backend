package com.semicolon.backend.domain.auth.service;

public interface MailService {
    String sendMail(String email);
    String sendJoinEmail(String email);
}
