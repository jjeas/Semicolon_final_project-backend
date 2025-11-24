package com.semicolon.backend.domain.faq.service;

import com.semicolon.backend.domain.faq.dto.FaqCategoryDTO;
import com.semicolon.backend.domain.faq.dto.FaqDTO;
import com.semicolon.backend.domain.faq.entity.FaqCategory;

import java.util.List;

public interface FaqService {
    public List<FaqDTO> getAll();
    public void update(FaqDTO dto, Long id);
    public void register(FaqDTO dto);
    public void delete(Long id);
    public List<FaqCategoryDTO>  getAllCategories();
}
