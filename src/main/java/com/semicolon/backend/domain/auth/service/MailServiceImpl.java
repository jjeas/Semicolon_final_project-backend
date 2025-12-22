package com.semicolon.backend.domain.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService{
    private final JavaMailSender sender; //메일 보내주는 자바 기능
    private static final String SENDER_EMAIL = "dlrjsgh951@gmail.com"; //메일 보내주는 이메일 설정

    public String createNumber(){
        return ""+(int)((Math.random()*100000)+600000); //인증번호 6자리 랜덤생성
    }
    @Override
    public String sendMail(String email) {
        String authNumber=createNumber(); //인증에 사용할 6자리 숫자
        MimeMessage message = sender.createMimeMessage(); //MIME 표준으로 자바 메일기능 생성
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true); //메일 생성 객체
            helper.setFrom(SENDER_EMAIL); //보내는 이메일 주소
            helper.setTo(email); //인증메일 보낼 주소
            helper.setSubject("[semicolon] 아이디/비밀번호 찾기 인증번호입니다."); //메일 제목
            helper.setText("<h1>인증번호: "+authNumber+"</h1>",true); //메일 내용
            sender.send(message); //메일 전송
            return authNumber; //인증번호 리턴
        }catch (MessagingException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String sendJoinEmail(String email) {
        String authNumber = createNumber();
        MimeMessage message = sender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(SENDER_EMAIL);
            helper.setTo(email);
            // ▼ 제목 변경
            helper.setSubject("[semicolon] 회원가입 인증번호입니다.");
            helper.setText("<h1>인증번호: " + authNumber + "</h1>", true);
            sender.send(message);
            return authNumber;
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
