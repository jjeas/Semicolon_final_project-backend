package com.semicolon.backend.domain.notice.service;


import com.semicolon.backend.domain.notice.dto.NoticeDTO;
import com.semicolon.backend.domain.notice.entity.Notice;
import com.semicolon.backend.global.pageable.PageRequestDTO;
import com.semicolon.backend.global.pageable.PageResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NoticeService {
    public void deleteNotice(Long noticeId);
    public PageResponseDTO<NoticeDTO> list(PageRequestDTO pageRequestDTO);
    public NoticeDTO getOne(Long noticeId) throws Exception;
    public void modify(Long id, NoticeDTO dto);
    public void increaseViewCount(Long id);
    public void registerNotice(NoticeDTO dto);
}
