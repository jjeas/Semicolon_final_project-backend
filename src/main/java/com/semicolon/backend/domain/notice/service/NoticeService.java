package com.semicolon.backend.domain.notice.service;

import com.semicolon.backend.domain.notice.dto.NoticeCreateRequest;
import com.semicolon.backend.domain.notice.dto.NoticeDTO;
import com.semicolon.backend.domain.notice.entity.Notice;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NoticeService {
    public void registerNotice(NoticeDTO noticeDTO);
    public void deleteNotice(Long noticeId);
    public List<NoticeDTO> list();
    public NoticeDTO getOne(Long noticeId) throws Exception;
    public void modify(Long id, NoticeDTO dto);
    public void increaseViewCount(Long id);

    public void registerAllNotice(NoticeDTO dto);
}
