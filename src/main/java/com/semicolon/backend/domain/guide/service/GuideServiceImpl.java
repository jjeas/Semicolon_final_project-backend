package com.semicolon.backend.domain.guide.service;

import com.semicolon.backend.domain.guide.dto.GuideDTO;
import com.semicolon.backend.domain.guide.entity.Guide;
import com.semicolon.backend.domain.guide.repository.GuideRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class GuideServiceImpl implements GuideService{

    private final GuideRepository guideRepository;

    @Override
    @Transactional
    public void save(GuideDTO guideDTO) {
        log.info("SAVE 요청: {}", guideDTO);
        Guide guide = guideRepository.findByCategory(guideDTO.getCategory())
                .orElse(null);

        if (guide != null) {
            log.info("기존 문서 수정: id = {}", guide.getId());
            guide.setHtml(guideDTO.getHtml());
            guide.setUpdatedDate(LocalDateTime.now());
            return;
        }

        log.info("신규 문서 생성: category = {}", guideDTO.getCategory());
        Guide newGuide = new Guide();
        newGuide.setCategory(guideDTO.getCategory());
        newGuide.setHtml(guideDTO.getHtml());
        newGuide.setUpdatedDate(LocalDateTime.now());

        guideRepository.save(newGuide);
    }

    @Override
    public GuideDTO get(String category) {
        Guide guide = guideRepository.findByCategory(category)
                .orElse(null);

        if (guide == null) {
            return new GuideDTO();
        }

        GuideDTO guideDTO = new GuideDTO();
        guideDTO.setId(guide.getId());
        guideDTO.setCategory(guide.getCategory());
        guideDTO.setHtml(guide.getHtml());
        guideDTO.setUpdatedDate(guide.getUpdatedDate());
        return guideDTO;
    }
}
