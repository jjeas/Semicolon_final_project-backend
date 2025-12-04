package com.semicolon.backend.domain.notice;

import com.semicolon.backend.domain.notice.dto.NoticeDTO;
import com.semicolon.backend.domain.notice.service.NoticeService;
import com.semicolon.backend.global.pageable.PageRequestDTO;
import com.semicolon.backend.global.pageable.PageResponseDTO;
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
    @DeleteMapping("/admin/{no}")
    public ResponseEntity<String> delete(@PathVariable("no") long no){
        log.info("공지 삭제기능 실행");
        service.deleteNotice(no);
        return ResponseEntity.ok("공지가 삭제되었습니다.");
    }
    @GetMapping("/{no}")
    public ResponseEntity<NoticeDTO> getOneNotice(@PathVariable("no") Long no) throws Exception{
        return ResponseEntity.ok(service.getOne(no));
    }
    @PutMapping("/admin/{no}")
    public ResponseEntity<String> modify(@PathVariable("no") Long no, @RequestBody NoticeDTO dto){
        service.modify(no, dto);
        return ResponseEntity.ok("공지 수정이 완료되었습니다.");
    }
    @GetMapping("/list")
    public ResponseEntity<PageResponseDTO<NoticeDTO>> getList(PageRequestDTO pageRequestDTO){
        log.info("컨트롤러 요청 파라미터 확인: page={}, size={}, type={}, keyword={}",
                pageRequestDTO.getPage(),
                pageRequestDTO.getSize(),
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword());
        return ResponseEntity.ok(service.list(pageRequestDTO));
    }

    @PostMapping("/{no}/view")
    public ResponseEntity<String> addViewCount(@PathVariable long no){
        service.increaseViewCount(no);
        return ResponseEntity.ok("조회수 1 증가");
    }

    @PostMapping("/admin/register")
    public ResponseEntity<String> registerAll(@RequestBody NoticeDTO dto){
        log.info("dto를 확인해요 => {}",dto);
        service.registerNotice(dto);
        return ResponseEntity.ok("성공");
    }
}
