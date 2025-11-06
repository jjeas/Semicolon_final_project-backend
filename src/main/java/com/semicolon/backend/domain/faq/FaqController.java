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
@RequestMapping("/api/faq")
public class FaqController {

    @Autowired
    private FaqService service;

    @GetMapping("/list")
    public List<FaqDTO> list(){
        return service.getAll();
    }
//    @PutMapping("/{id}")
//    public ResponseEntity<String> update(@RequestBody FaqDTO dto, @PathVariable("id") long id){
//        service.update(dto,id);
//        return ResponseEntity.ok("FAQ 업데이트 성공");
//    }
//    @PostMapping("/modify")
//    public ResponseEntity<String> modify(@RequestBody FaqDTO dto){
//        service.modify(dto);
//        return ResponseEntity.ok("FAQ 등록 성공");
//    }
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> delete(@PathVariable("id") Long id){
//        service.delete(id);
//        return ResponseEntity.ok("FAQ 삭제 완료");
//    }
    @GetMapping("/category")
    public ResponseEntity<List<FaqCategoryDTO>> categoryList(){
        return ResponseEntity.ok(service.getAllCategories());
    }
}
