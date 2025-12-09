package com.semicolon.backend.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ChatResDTO {
    private Long roomId;
    private String memberName;
    private boolean isReplied;
    private String lastMessage;
    private LocalDateTime lastSendAt;
}
