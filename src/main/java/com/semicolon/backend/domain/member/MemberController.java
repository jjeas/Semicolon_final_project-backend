package com.semicolon.backend.domain.member;

import com.semicolon.backend.domain.member.dto.MemberDTO;
import com.semicolon.backend.domain.member.service.MemberService;
import com.semicolon.backend.domain.partner.dto.PartnerDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService service;

    @GetMapping("/{id}/memberEdit")
    public ResponseEntity<MemberDTO> memberDTOResponseEntity (@PathVariable("id") Long memberId) {
        return ResponseEntity.ok(service.getOne(memberId));
    }

    @PostMapping("/{id}/memberEdit")
    public ResponseEntity<String> memberDTO (@PathVariable("id") Long memberId, @RequestBody MemberDTO memberDTO) {
        service.register(memberDTO);
        return ResponseEntity.ok("Member 수정 완료");
    }

    @GetMapping("/{id}/partnerRequest")
    public PartnerDTO partnerStatus(@PathVariable("id") Long memberId){
        return service.getPartnerStatus(memberId);
    }

}

