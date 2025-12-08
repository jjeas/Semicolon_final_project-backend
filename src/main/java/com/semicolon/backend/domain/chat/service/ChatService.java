package com.semicolon.backend.domain.chat.service;

import com.semicolon.backend.domain.chat.dto.ChatDTO;

import java.util.List;

public interface ChatService {
    void saveMessage(ChatDTO dto, String loginId);
    List<Long> getRoomList();
    List<ChatDTO> getChatHistory(Long roomId);
}
