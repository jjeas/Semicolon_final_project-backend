package com.semicolon.backend.domain.guide.service;

import com.semicolon.backend.domain.guide.dto.GuideDTO;
import com.semicolon.backend.domain.guide.entity.Guide;
import com.semicolon.backend.domain.guide.repository.GuideRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class GuideServiceImpl implements GuideService{

    private final GuideRepository guideRepository;

    @Override
    public void save(GuideDTO guideDTO) {
        Guide guide = guideRepository.findByCategory(guideDTO.getCategory()).orElse(new Guide());
        guide.setId(guideDTO.getId());
        guide.setCategory(guideDTO.getCategory());
        guide.setHtml(guideDTO.getHtml());
        guide.setUpdatedDate(LocalDateTime.now());

        guideRepository.save(guide);
    }

    @Override
    public GuideDTO get(String category) {
        Guide guide = guideRepository.findByCategory(category).orElse(new Guide());
        GuideDTO guideDTO = new GuideDTO();
        guideDTO.setId(guide.getId());
        guideDTO.setCategory(guide.getCategory());
        guideDTO.setHtml(guide.getHtml());
        guideDTO.setUpdatedDate(guide.getUpdatedDate());
        return guideDTO;
    }
}
