package com.semicolon.backend.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> noSuchElement(NoSuchElementException e){
        return ResponseEntity
                .badRequest()
                .body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> illegalArgument(IllegalArgumentException e){
        return ResponseEntity
                .badRequest()
                .body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> validateException(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();

        // 첫 번째 에러 메시지만 뽑아서 보냄 (예: "비밀번호는 영문, 숫자...")
        String firstErrorMessage = bindingResult.getFieldError().getDefaultMessage();

        return ResponseEntity.badRequest().body(firstErrorMessage);
    }
}
