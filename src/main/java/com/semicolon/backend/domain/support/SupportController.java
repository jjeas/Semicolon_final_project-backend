package com.semicolon.backend.domain.support;

import com.semicolon.backend.domain.support.dto.SupportDTO;
import com.semicolon.backend.domain.support.dto.SupportUploadDTO;
import com.semicolon.backend.domain.support.service.SupportService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/member")
@Slf4j
@RequiredArgsConstructor
public class SupportController {

    private final SupportService service;

    @PostMapping("/{id}/support/write")
    public ResponseEntity<String> memberSupportReq (@PathVariable("id") Long id, @ModelAttribute SupportDTO supportDTO){
        service.supportReqRegister(supportDTO);
        log.info("프론트에서 받은 support 내용 => {}", supportDTO);
        return ResponseEntity.ok("문의 제출 완료");
    }

    @GetMapping("/{id}/support")
    public List<SupportUploadDTO> supportUploadDTO (@PathVariable("id") Long id){
        return service.getSupportList(id);
    }

    @GetMapping("/{id}/support/{no}")
    public SupportUploadDTO supportUploadDTO (@PathVariable("id") Long id, @PathVariable("no") Long no) {
        return service.getOneSupport(id, no);
    }

    @GetMapping("/support/view/{fileName}")
    public Resource view(@PathVariable String fileName) {
        return new FileSystemResource("C:/dev/upload/supportFiles/" + fileName);
    }
}
