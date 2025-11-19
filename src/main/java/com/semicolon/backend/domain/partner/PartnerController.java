package com.semicolon.backend.domain.partner;

import com.semicolon.backend.domain.partner.dto.PartnerDTO;
import com.semicolon.backend.domain.partner.dto.PartnerUploadDTO;
import com.semicolon.backend.domain.partner.entity.PartnerStatus;
import com.semicolon.backend.domain.partner.service.PartnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/partnerRequest")
    public ResponseEntity<List<PartnerUploadDTO>> getList(){
        return ResponseEntity.ok(service.getList());
    }

    @GetMapping("/partnerRequest/{id}")
    public ResponseEntity<PartnerUploadDTO> getOne(@PathVariable("id") Long id){
        return ResponseEntity.ok(service.getOne(id));
    }

    @PostMapping("/partnerRequest/{id}")
    public ResponseEntity<String> changeStatus(@PathVariable("id") Long id, @RequestBody PartnerStatus status){
        service.changeStatus(id,status);
        return ResponseEntity.ok("상태 변경 완료");
    }

}

