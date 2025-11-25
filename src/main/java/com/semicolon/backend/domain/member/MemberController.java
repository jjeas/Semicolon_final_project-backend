package com.semicolon.backend.domain.member;

import com.semicolon.backend.domain.member.dto.MemberDTO;
import com.semicolon.backend.domain.member.dto.PasswordChangeDTO;
import com.semicolon.backend.domain.member.service.MemberService;
import com.semicolon.backend.domain.partner.dto.PartnerDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService service;

    @GetMapping("/memberEdit")
    public ResponseEntity<MemberDTO> getMemberInfo (@AuthenticationPrincipal String loginIdFromToken) {
        //AuthenticationPrincipal 어노테이션은 콘텍스트홀더에 담긴 유저 정보 중 아이디를 가져옵니다
        MemberDTO dto = service.getOneByLoginId(loginIdFromToken);
        //아이디로 dto를 하나 가져와서 req 에 담아서 보내줍니다
        //멤버id를 직접적으로 요청에 담는것은 보안상 위험하기에 이 방식으로 변경해봤습니다
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/memberEdit")
    public ResponseEntity<String> modifyMember (@AuthenticationPrincipal String loginIdFromToken, @RequestBody MemberDTO requestDTO) {
        MemberDTO dtoBefore = service.getOneByLoginId(loginIdFromToken);
        service.modify(loginIdFromToken, requestDTO);
        return ResponseEntity.ok("Member 수정 완료");
      
    @PostMapping("/{id}/memberEdit")
    public ResponseEntity<String> memberDTO (@PathVariable("id") Long memberId, @RequestBody MemberDTO memberDTO) {
        service.register(memberDTO);
        return ResponseEntity.ok("Member 수정 중");
    }

    @GetMapping("/{id}/partnerRequest")
    public PartnerDTO partnerStatus(@PathVariable("id") Long memberId){
        return service.getPartnerStatus(memberId);
    }

    @PostMapping("/{id}/passwordEdit")
    public ResponseEntity<String> changePassword(@PathVariable("id") Long memberId, @RequestBody PasswordChangeDTO passwordChangeDTO) {
        service.changePassword(memberId, passwordChangeDTO);
        return ResponseEntity.ok("password 수정 중");
    }

}

