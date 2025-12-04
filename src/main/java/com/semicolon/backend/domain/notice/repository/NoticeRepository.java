package com.semicolon.backend.domain.notice.repository;

import com.semicolon.backend.domain.notice.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Page<Notice> findByTitleContaining(String keyword, Pageable pageable);
    Page<Notice> findByContentContaining(String keyword, Pageable pageable);
    Page<Notice> findByContentContainingOrTitleContaining(String content, String title, Pageable pageable);
}
