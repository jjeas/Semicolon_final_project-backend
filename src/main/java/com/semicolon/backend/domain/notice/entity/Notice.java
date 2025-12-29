package com.semicolon.backend.domain.notice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_notice")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long noticeId;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "content",nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "view_count",nullable = false)
    @Builder.Default
    private int viewCount=0;

    @OneToMany(mappedBy = "notice",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<NoticeFile> files = new ArrayList<>();

    public void addFile(NoticeFile file){
        files.add(file);
        file.setNotice(this);
    }

    public void removeFile(NoticeFile file) {
        this.files.remove(file);
        file.setNotice(null);
    }


}
