package com.semicolon.backend.domain.faq.service;

import com.semicolon.backend.domain.faq.dto.FaqDTO;
import com.semicolon.backend.domain.faq.entity.Faq;
import com.semicolon.backend.domain.faq.repository.FaqRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FaqServiceImpl implements FaqService{
    @Autowired
    private FaqRepository repository;

    public FaqDTO toDto(Faq faq){
        return FaqDTO.builder()
                    .faqId(faq.getFaqId())
                    .question(faq.getQuestion())
                    .answer(faq.getAnswer())
                    .category(faq.getCategory())
                    .createdAt(faq.getCreatedAt())
                    .updatedAt(faq.getUpdatedAt())
                    .build();
    }
    @Override
    public List<FaqDTO> getAll() {
        return repository.findAll().stream().map(faq -> toDto(faq)).toList();
    }

    @Override
    public void update(FaqDTO dto, Long id) {
        Faq faq = repository.findById(id).orElseThrow(()->new IllegalArgumentException("일치하는 FAQ가 존재하지 않습니다."));
        faq.setQuestion(dto.getQuestion());
        faq.setAnswer(dto.getAnswer());
        faq.setUpdatedAt(LocalDateTime.now());
        faq.setCategory(dto.getCategory());
        repository.save(faq);
    }

    @Override
    public void modify(FaqDTO dto) {
        Faq faq = Faq.builder()
                .question(dto.getQuestion())
                .answer(dto.getAnswer())
                .category(dto.getCategory())
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .build();
        repository.save(faq);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
