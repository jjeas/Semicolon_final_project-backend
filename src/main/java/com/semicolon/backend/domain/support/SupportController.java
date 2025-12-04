package com.semicolon.backend.domain.support;

import com.semicolon.backend.domain.member.service.MemberService;
import com.semicolon.backend.domain.support.dto.SupportDTO;
import com.semicolon.backend.domain.support.dto.SupportResponseDTO;
import com.semicolon.backend.domain.support.dto.SupportUploadDTO;
import com.semicolon.backend.domain.support.service.SupportService;
import com.semicolon.backend.global.pageable.PageRequestDTO;
import com.semicolon.backend.global.pageable.PageResponseDTO;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/support")
@Slf4j
@RequiredArgsConstructor
public class SupportController {

    private final SupportService service;
    private final MemberService memberService;

    @PostMapping("/write")
    public ResponseEntity<String> memberSupportReq (@ModelAttribute SupportDTO supportDTO, @AuthenticationPrincipal String loginIdFromToken){
        log.info("loginIdFromToken ====>{}",loginIdFromToken);
        log.info("프론트에서 받은 support 내용 => {}", supportDTO);
        service.supportReqRegister(loginIdFromToken,supportDTO);
        return ResponseEntity.ok("문의 제출 완료");
    }

    @GetMapping("")
    public List<SupportUploadDTO> supportUploadDTOList (@AuthenticationPrincipal String loginIdFromToken){
        Long id = memberService.getOneByLoginId(loginIdFromToken).getMemberId();
        return service.getSupportList(id);
    }

    @GetMapping("/{no}")
    public SupportUploadDTO supportUploadDTO (@PathVariable("no") Long no) {
        return service.getOneSupport(no);
    }

    @GetMapping("/view/{fileName}")
    public Resource view(@PathVariable String fileName) {
        return new FileSystemResource("C:/dev/upload/supportFiles/" + fileName);
    }

    @GetMapping("/all")
    public ResponseEntity<PageResponseDTO<SupportUploadDTO>> getAll(PageRequestDTO pageRequestDTO){
        return ResponseEntity.ok(service.getSupportAllList(pageRequestDTO));
    }

    @PostMapping("/{no}")
    public List<SupportResponseDTO> registerResponse(@PathVariable("no") Long no, @RequestBody SupportResponseDTO dto){
        return service.registerResponse(no, dto);
    }
}
