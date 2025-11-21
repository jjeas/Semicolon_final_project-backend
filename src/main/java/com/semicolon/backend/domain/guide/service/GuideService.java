package com.semicolon.backend.domain.guide.service;

import com.semicolon.backend.domain.guide.dto.GuideDTO;
import com.semicolon.backend.domain.guide.dto.GuideReqDTO;
import com.semicolon.backend.domain.guide.entity.GuideCategory;

public interface GuideService {
    public GuideDTO get(GuideCategory category);
    public void upload(GuideReqDTO guideReqDTO);
}
