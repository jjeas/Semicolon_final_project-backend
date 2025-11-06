package com.semicolon.backend.domain.notice.service;

import com.semicolon.backend.domain.notice.dto.NoticeDTO;
import com.semicolon.backend.domain.notice.entity.Notice;
import com.semicolon.backend.domain.notice.repository.NoticeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class NoticeServiceImpl implements NoticeService{
    @Autowired
    private NoticeRepository repository;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");

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
                .noticeId(notice.getNoticeId())
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
        notice.setContent(dto.getContent());
        notice.setTitle(dto.getTitle());
        repository.save(notice);
    }

    @Override
    public void increaseViewCount(Long id) {
        Notice notice = repository.findById(id).orElseThrow(()->new IllegalArgumentException("해당하는 공지사항이 없습니다."));
        notice.setViewCount(notice.getViewCount()+1);
        log.info("조회수 1 증가 공지={}",notice);
        repository.save(notice);
    }
}
