package com.semicolon.backend.domain.notice;

import com.semicolon.backend.domain.notice.dto.NoticeDTO;
import com.semicolon.backend.domain.notice.service.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/community/notice")
public class NoticeController {
    @Autowired
    private NoticeService service;

//    @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody NoticeDTO noticeDTO){
//        service.registerNotice(noticeDTO);
//        return ResponseEntity.ok("공지가 저장되었습니다.");
//    }
    @DeleteMapping("/{id}/admin")
    public ResponseEntity<String> delete(@PathVariable("id") long id){
        log.info("공지 삭제기능 실행");
        service.deleteNotice(id);
        return ResponseEntity.ok("공지가 삭제되었습니다.");
    }
    @GetMapping("/{id}")
    public ResponseEntity<NoticeDTO> getOneNotice(@PathVariable("id") Long noticeId) throws Exception{
        return ResponseEntity.ok(service.getOne(noticeId));
    }
    @PutMapping("/{id}/admin")
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

    @PostMapping("/register/admin")
    public ResponseEntity<String> registerAll(@RequestBody NoticeDTO dto){
        log.info("dto를 확인해요 => {}",dto);
        service.registerNotice(dto);
        return ResponseEntity.ok("성공");
    }
}
