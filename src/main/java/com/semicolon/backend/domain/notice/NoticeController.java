package com.semicolon.backend.domain.notice;

import com.semicolon.backend.domain.notice.dto.NoticeCreateRequest;
import com.semicolon.backend.domain.notice.dto.NoticeDTO;
import com.semicolon.backend.domain.notice.service.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/notice")
public class NoticeController {
    @Autowired
    private NoticeService service;

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id){
        service.deleteNotice(id);
        return ResponseEntity.ok("공지가 삭제되었습니다.");
    }
    @GetMapping("/{id}")
    public ResponseEntity<NoticeDTO> getOneNotice(@PathVariable("id") Long noticeId) throws Exception{
        return ResponseEntity.ok(service.getOne(noticeId));
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> modify(@PathVariable("id") Long id, @RequestBody NoticeDTO dto){
        service.modify(id, dto);
        return ResponseEntity.ok("공지 수정이 완료되었습니다.");
    }
    @GetMapping("/list")
    public ResponseEntity<List<NoticeDTO>> getList(){
        return ResponseEntity.ok(service.list());
    }
    @PostMapping("/{id}/view")
    public ResponseEntity<String> addViewCount(@PathVariable long id){
        service.increaseViewCount(id);
        return ResponseEntity.ok("조회수 1 증가");
    }
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> registerAll(@ModelAttribute NoticeDTO dto){
        log.info("dto를 확인해요 => {}",dto);
        service.registerNotice(dto);
        return ResponseEntity.ok("성공");
    }
}
