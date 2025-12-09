package com.semicolon.backend.domain.chat.entity;

import com.semicolon.backend.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CHAT_SEQ_GEN")
    @SequenceGenerator(
            name = "CHAT_SEQ_GEN",
            sequenceName = "SEQ_CHAT", // DB에 생길 시퀀스 이름
            allocationSize = 1 // 1씩 증가 (오라클 기본값 50 문제 방지)
    )
    private Long id;

    @Column(nullable = false)
    private String message;
    @JoinColumn(nullable = false, name = "sender_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member sender;
    @Column(nullable = false)
    private Long roomId;
    @Column(nullable = false)
    private LocalDateTime sendAt;
    @Column(nullable = false)
    private boolean isRead;

}
