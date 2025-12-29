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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
