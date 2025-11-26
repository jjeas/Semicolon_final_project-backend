package com.semicolon.backend.domain.faq;

import com.semicolon.backend.domain.faq.dto.FaqCategoryDTO;
import com.semicolon.backend.domain.faq.dto.FaqDTO;
import com.semicolon.backend.domain.faq.entity.FaqCategory;
import com.semicolon.backend.domain.faq.service.FaqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community/faq")
public class FaqController {

    @Autowired
    private FaqService service;

    @GetMapping("/list")
    public List<FaqDTO> list(){
        return service.getAll();
    }

    @GetMapping("/category")
    public ResponseEntity<List<FaqCategoryDTO>> categoryList(){
        return ResponseEntity.ok(service.getAllCategories());
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<String> updateFaq(@RequestBody FaqDTO dto, @PathVariable("id") long id){
        service.update(dto,id);
        return ResponseEntity.ok("FAQ 업데이트 성공");
    }

    @PostMapping("/admin/register")
    public ResponseEntity<String> registerFaq(@RequestBody FaqDTO dto){
        service.register(dto);
        return ResponseEntity.ok("FAQ 등록 성공");
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<String> deleteFaq(@PathVariable("id") Long id){
        service.delete(id);
        return ResponseEntity.ok("FAQ 삭제 완료");
    }
}
