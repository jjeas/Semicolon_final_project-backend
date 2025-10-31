package com.semicolon.backend.domain.notice;

import com.semicolon.backend.domain.notice.dto.NoticeCreateRequest;
import com.semicolon.backend.domain.notice.dto.NoticeDTO;
import com.semicolon.backend.domain.notice.service.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/notice")
public class NoticeController {
    @Autowired
    private NoticeService service;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody NoticeDTO noticeDTO){
        service.registerNotice(noticeDTO);
        return ResponseEntity.ok("공지가 저장되었습니다.");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id){
        service.deleteNotice(id);
        return ResponseEntity.ok("공지가 삭제되었습니다.");
    }
    @GetMapping("/{id}")
    public ResponseEntity<NoticeDTO> getOneNotice(@PathVariable("id") Long noticeId) throws Exception{
        return ResponseEntity.ok(service.getOne(noticeId));
    }
    @PutMapping("{id}")
    public ResponseEntity<String> modify(@PathVariable("id") Long id, @RequestBody NoticeDTO dto){
        service.modify(id, dto);
        return ResponseEntity.ok("공지 수정이 완료되었습니다.");
    }
}
