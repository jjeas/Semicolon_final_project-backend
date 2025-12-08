package com.semicolon.backend.domain.registration;

import com.semicolon.backend.domain.member.repository.MemberRepository;
import com.semicolon.backend.domain.registration.dto.RegistrationDTO;
import com.semicolon.backend.domain.registration.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService service;
    @GetMapping("")
    public ResponseEntity<List<RegistrationDTO>> get(@AuthenticationPrincipal String loginIdFromToken){
        return ResponseEntity.ok(service.getList(loginIdFromToken));
    }

    @PostMapping("/{lessonId}")
    public ResponseEntity<String> register(@AuthenticationPrincipal String loginIdFromToken, @PathVariable("lessonId") Long id){
        try {
            service.register(id,loginIdFromToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("수강 신청이 완료되었습니다.");
    }

    @DeleteMapping("/cancel/{registrationId}")
    public ResponseEntity<String> cancel(@AuthenticationPrincipal String loginIdFromToken, @PathVariable("registrationId") Long registrationId){
        service.cancel(loginIdFromToken, registrationId);
        return ResponseEntity.ok("수강 신청 취소가 완료되었습니다.");
    }

    @GetMapping("/check/{lessonId}")
    public ResponseEntity<Boolean> checkStatus(
            @AuthenticationPrincipal String loginIdFromToken,
            @PathVariable("lessonId") Long lessonId
    ) {
        log.info("들어오냐?");
        boolean isRegistered = service.checkRegistrationStatus(loginIdFromToken, lessonId);
        return ResponseEntity.ok(isRegistered);
    }
}
