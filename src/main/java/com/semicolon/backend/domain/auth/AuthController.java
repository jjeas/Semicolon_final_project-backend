package com.semicolon.backend.domain.auth;

import com.semicolon.backend.domain.auth.dto.*;
import com.semicolon.backend.domain.auth.service.AuthService;
import com.semicolon.backend.domain.auth.service.MailService;
import com.semicolon.backend.domain.member.dto.MemberDTO;
import com.semicolon.backend.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@Slf4j
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final MailService mailService;
    private static final Map<String, String> AUTH_CODE_MAP = new ConcurrentHashMap<String, String>();

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
        LoginResponse response = authService.login(
                request.getLoginId(),
                request.getPassword()
        );
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid MemberDTO dto){
        authService.register(dto);
        return ResponseEntity.ok("신규 회원가입 성공");
    }

    @PostMapping("/sendCode")
    public ResponseEntity<String> sendCode(@RequestBody FindIdRequestDTO dto){
        authService.findMemberId(dto);
        //dto 를 활용해 멤버에 있는지 여부 체크
        //없으면 아래로 못넘어감
        String authCode = mailService.sendMail(dto.getMemberEmail());
        //프론트에서 보낸 이메일로 메일 발송
        AUTH_CODE_MAP.put(dto.getMemberEmail(), authCode);
        // 이메일을 키로, 인증번호를 밸류로 static 선언된 인증코드에 저장
        return ResponseEntity.ok("인증번호가 발송되었습니다.");
    }

    @PostMapping("/checkCode")
    public ResponseEntity<String> returnMemerId(@RequestBody IdCheckCodeDTO dto){
        String code = AUTH_CODE_MAP.get(dto.getMemberEmail());
        //이메일 입력으로 코드 빼내기
        if(code!=null && code.equals(dto.getAuthCode())){ //코드 유효성 검사 후
            FindIdRequestDTO findDto= FindIdRequestDTO.builder()
                    .memberName(dto.getMemberName())
                    .memberEmail(dto.getMemberEmail())
                    .build(); //전달받은 dto 정보로 id찾는DTO 생성
            AUTH_CODE_MAP.remove(dto.getMemberEmail());//저장된 인증코드 삭제(재활용해야하니)
            String memberId = authService.findMemberId(findDto);//findIDDTO 로 아이디 찾기
            return ResponseEntity.ok(memberId);//아이디 반환
        }else{
            return ResponseEntity.badRequest().body("인증번호가 일치하지 않습니다.");
        }
    }
    @PostMapping("/sendCodePw")
    public ResponseEntity<String> sendCodePw(@RequestBody FindPwRequestDTO dto){//비밀번호 찾기 시 메일 발송하는 코드
        boolean exists = authService.findMemberPw(dto);//dto 정보 바탕으로 DB 에 실제 있는 유저인지 확인
        if(!exists){
            return ResponseEntity.badRequest().body("일치하는 회원이 없습니다."); //없으면 일치하는 회원 없다고 알림
        }
        String authCode = mailService.sendMail(dto.getMemberEmail());//입력한 이메일로 메일 전송
        AUTH_CODE_MAP.put(dto.getMemberEmail(), authCode);//스태틱 메모리 정보에 메일과 인증코드 넣기
        return ResponseEntity.ok("인증번호가 발송되었습니다.");
    }
    @PostMapping("/checkCodePw")
    public ResponseEntity<String> resetPassword(@RequestBody PwCheckCodeDTO dto){ //인증하는 컨트롤러
        String code = AUTH_CODE_MAP.get(dto.getMemberEmail()); //스태틱 메모리로 저장된 코드 꺼내기
        if(code!=null && code.equals(dto.getAuthCode())){//코드 유효성 검사 한번 더 하기
            return ResponseEntity.ok("인증에 성공했습니다."); //인증 성공 시 반환
        }else{
            return ResponseEntity.badRequest().body("인증번호가 일치하지 않습니다.");//인증번호 미일치 시 반환
        }
    }
    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDTO dto){
        String authCode = AUTH_CODE_MAP.get(dto.getMemberEmail()); //인증코드 가져오기
        if(authCode!=null && authCode.equals(dto.getAuthCode())){ //유효성 검사 한번 더 하기
            authService.changePassword(dto);//인자로 받은 정보 바탕으로 비밀번호 변경
            AUTH_CODE_MAP.remove(dto.getMemberEmail());//스태틱 메모리에서 기존 인증코드 제거
            return ResponseEntity.ok("비밀번호 변경이 완료되었습니다."); //비밀번호 변경 완료
        }else{
            return ResponseEntity.badRequest().body("비밀번호 변경 중 오류가 발생했습니다.");
        }
    }
    @GetMapping("/check/email")
    public ResponseEntity<Boolean> checkDuplicateEmail(@RequestParam String email){
        return ResponseEntity.ok(authService.checkEmailDuplicate(email));
    }
    @GetMapping("/check/loginId")
    public ResponseEntity<Boolean> checkDuplicateLoginId(@RequestParam String loginId){
        return ResponseEntity.ok(authService.checkLoginIdDuplicate(loginId));
    }

    @GetMapping("/join/email")
    public ResponseEntity<String> sendJoinMail(@RequestParam String email){
        if(authService.checkEmailDuplicate(email)){
            return ResponseEntity.badRequest().body("이미 존재하는 이메일입니다.");
        }
        String authCode = mailService.sendJoinEmail(email);
        AUTH_CODE_MAP.put(email, authCode);
        return ResponseEntity.ok("인증번호가 발송되었습니다.");
    }

    @PostMapping("/join/verify")
    public ResponseEntity<String> verifyJoinCode(@RequestBody EmailCheckDTO dto){
        String code = AUTH_CODE_MAP.get(dto.getMemberEmail());
        if(code!=null && code.equals(dto.getAuthCode())){
            return ResponseEntity.ok("인증이 완료되었습니다.");
        }else {
            return ResponseEntity.badRequest().body("인증번호가 일치하지 않습니다.");
        }
    }
}
