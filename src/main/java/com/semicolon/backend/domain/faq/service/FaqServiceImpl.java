package com.semicolon.backend.domain.faq.service;

import com.semicolon.backend.domain.faq.dto.FaqCategoryDTO;
import com.semicolon.backend.domain.faq.dto.FaqDTO;
import com.semicolon.backend.domain.faq.entity.Faq;
import com.semicolon.backend.domain.faq.entity.FaqCategory;
import com.semicolon.backend.domain.faq.repository.FaqCategoryRepository;
import com.semicolon.backend.domain.faq.repository.FaqRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FaqServiceImpl implements FaqService{

    private final FaqRepository repository;
    private final FaqCategoryRepository categoryRepository;

    public FaqDTO toDto(Faq faq){
        return FaqDTO.builder()
                    .faqId(faq.getFaqId())
                    .question(faq.getQuestion())
                    .answer(faq.getAnswer())
                    .createdAt(faq.getCreatedAt())
                    .updatedAt(faq.getUpdatedAt())
                    .faqCategoryId(faq.getFaqCategory().getFaqCategoryId())
                    .categoryName(faq.getFaqCategory().getCategoryName())
                    .build();
    }
    @Override
    public List<FaqDTO> getAll() {
        return repository.findAll().stream().map(faq -> toDto(faq)).toList();
    }

    @Override
    public List<FaqCategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream().map(i->new FaqCategoryDTO(i.getFaqCategoryId(),i.getCategoryName())).toList();
    }

    @Override
    public void update(FaqDTO dto, Long id) {
        Faq faq = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 FAQ가 존재하지 않습니다."));

        FaqCategory category = categoryRepository.findById(dto.getFaqCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        faq.setQuestion(dto.getQuestion());
        faq.setAnswer(dto.getAnswer());
        faq.setFaqCategory(category);
        faq.setUpdatedAt(LocalDateTime.now());

         repository.save(faq);
    }

    @Override
    public void register(FaqDTO dto) {
        log.info("FaqDTO ======> {}",dto);
        FaqCategory category = categoryRepository.findById(dto.getFaqCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        Faq faq = Faq.builder()
                .question(dto.getQuestion())
                .answer(dto.getAnswer())
                .faqCategory(category)
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
