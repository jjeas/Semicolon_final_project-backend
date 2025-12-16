package com.semicolon.backend.domain.chat.dto;

import com.semicolon.backend.domain.member.entity.MemberRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ChatResDTO {
    private Long roomId;
    private String senderId;
    private MemberRole senderRole;
    private String lastMessage;
    private LocalDateTime lastSendAt;
}
