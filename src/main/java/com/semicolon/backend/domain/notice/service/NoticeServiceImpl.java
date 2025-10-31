package com.semicolon.backend.domain.notice.service;

import com.semicolon.backend.domain.notice.dto.NoticeDTO;
import com.semicolon.backend.domain.notice.entity.Notice;
import com.semicolon.backend.domain.notice.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NoticeServiceImpl implements NoticeService{
    @Autowired
    private NoticeRepository repository;

    public Notice toEntity(NoticeDTO dto){
        return Notice.builder()
                .content(dto.getContent())
                .title(dto.getTitle())
                .createdAt(LocalDateTime.now())
                .viewCount(0)
                .build();
    }
    public NoticeDTO toDto(Notice notice){
        return NoticeDTO.builder()
                .content(notice.getContent())
                .title(notice.getTitle())
                .createdAt(notice.getCreatedAt())
                .viewCount(notice.getViewCount())
                .build();
    }

    @Override
    public void registerNotice(NoticeDTO dto) {
        repository.save(toEntity(dto));
    }

    @Override
    public void deleteNotice(Long noticeId) {
        repository.deleteById(noticeId);
    }

    @Override
    public List<NoticeDTO> list() {
        return repository.findAll().stream().map(notice->toDto(notice)).toList();
    }

    @Override
    public NoticeDTO getOne(Long noticeId) throws Exception{
        Notice notice =repository.findById(noticeId).orElseThrow(()->new IllegalArgumentException("해당 ID에 해당하는 공지사항이 없습니다."));
        return toDto(notice);
    }

    @Override
    public void modify(Long id, NoticeDTO dto) {
        Notice notice = repository.findById(id).orElseThrow();
        notice.builder()
                .title(dto.getTitle())
                .content(dto.getTitle())
                .build();
        repository.save(notice);
    }
}
