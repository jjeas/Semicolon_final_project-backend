package com.semicolon.backend.domain.chat.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatDTO {
    private Long roomId;
    private String message;
    private String sender;
}
