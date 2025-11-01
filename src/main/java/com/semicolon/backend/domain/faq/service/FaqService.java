package com.semicolon.backend.domain.faq.service;

import com.semicolon.backend.domain.faq.dto.FaqDTO;

import java.util.List;

public interface FaqService {
    public List<FaqDTO> getAll();
    public void update(FaqDTO dto, Long id);
    public void modify(FaqDTO dto);
    public void delete(Long id);
}
