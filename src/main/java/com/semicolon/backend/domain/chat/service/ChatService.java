package com.semicolon.backend.domain.chat.service;

import com.semicolon.backend.domain.chat.dto.ChatDTO;
import com.semicolon.backend.domain.chat.dto.ChatResDTO;

import java.util.List;

public interface ChatService {
    void saveMessage(ChatDTO dto, String loginId);
    List<ChatResDTO> getRoomList();
    List<ChatDTO> getChatHistory(Long roomId);
}
