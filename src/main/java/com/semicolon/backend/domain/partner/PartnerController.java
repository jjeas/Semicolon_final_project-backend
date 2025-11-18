package com.semicolon.backend.domain.partner;

import com.semicolon.backend.domain.partner.dto.PartnerDTO;
import com.semicolon.backend.domain.partner.service.PartnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService service;

    @PostMapping("/{id}/partnerRequest")
    public ResponseEntity<String> requestPartner(@PathVariable("id") Long id, @ModelAttribute PartnerDTO dto) {
        service.requestPartnerForm(dto);
        log.info("파트너 들어오는지 확인 => {}", dto);
        return ResponseEntity.ok("requestPartner 성공");
    }

}

