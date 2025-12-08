package com.semicolon.backend.domain.partner;

import com.semicolon.backend.domain.member.service.MemberService;
import com.semicolon.backend.domain.partner.dto.PartnerDTO;
import com.semicolon.backend.domain.partner.dto.PartnerUploadDTO;
import com.semicolon.backend.domain.partner.entity.PartnerStatus;
import com.semicolon.backend.domain.partner.service.PartnerService;
import com.semicolon.backend.global.pageable.PageRequestDTO;
import com.semicolon.backend.global.pageable.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/partner")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService service;
    private final MemberService memberService;

    @PostMapping("/partnerRequest")
    public ResponseEntity<String> requestPartner(@AuthenticationPrincipal String loginIdFromToken, @ModelAttribute PartnerDTO dto) {
        Long id = memberService.getOneByLoginId(loginIdFromToken).getMemberId();
        service.requestPartnerForm(id, dto);
        log.info("파트너 들어오는지 확인 => {}", dto);
        return ResponseEntity.ok("requestPartner 성공");
    }

    @GetMapping("/partnerRequest")
    public ResponseEntity<PageResponseDTO<PartnerUploadDTO>> getList(PageRequestDTO pageRequestDTO){
        return ResponseEntity.ok(service.getList(pageRequestDTO));
    }

    @GetMapping("/partnerRequest/{id}")
    public ResponseEntity<PartnerUploadDTO> getOne(@PathVariable("id") Long id){
        return ResponseEntity.ok(service.getOne(id));
    }

    @PostMapping("/partnerRequest/status/{id}")
    public ResponseEntity<String> changeStatus(@PathVariable("id") Long id, @RequestBody PartnerStatus status){
        service.changeStatus(id,status);
        return ResponseEntity.ok("상태 변경 완료");
    }

    @GetMapping("/class")
    public ResponseEntity<List<String>> getPartnerClassList(@AuthenticationPrincipal String loginIdFromToken){
        log.info("파트너 클래스 리스트 컨트롤러 실행 로그인id={}",loginIdFromToken);
        return ResponseEntity.ok(service.getPartnerClassList(loginIdFromToken));
    }
}

