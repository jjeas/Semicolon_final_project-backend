package com.semicolon.backend.domain.guide.service;

import com.semicolon.backend.domain.guide.dto.GuideDTO;

public interface GuideService {
    public void save(GuideDTO guideDTO);
    public GuideDTO get(String category);
}
