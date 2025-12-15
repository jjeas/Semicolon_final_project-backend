package com.semicolon.backend.domain.chat;

import com.semicolon.backend.domain.chat.dto.ChatDTO;
import com.semicolon.backend.domain.chat.dto.ChatResDTO;
import com.semicolon.backend.domain.chat.repository.ChatRepository;
import com.semicolon.backend.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final SimpMessageSendingOperations template;
    private final ChatService service;

    @Transactional
    @MessageMapping("/chat/message")
    public void message(ChatDTO dto, Principal principal){
        log.info("컨트롤러 도착! 메시지:{} " , dto.getMessage());
        if (principal == null) {
            log.warn("인증 정보가 없습니다.");
            return;
        }
        service.saveMessage(dto, principal.getName());
        dto.setSender(principal.getName());
        template.convertAndSend("/sub/chat/room/"+dto.getRoomId(), dto);
    }

    @GetMapping("/chat/list/admin")
    public List<ChatResDTO> getChatList(){
        log.info("채팅 리스트 컨트롤러 접속");
        return service.getRoomList();
    }

    @GetMapping("/chat/{id}")
    public List<ChatDTO> getChatHistory(@PathVariable("id") Long id){
        return service.getChatHistory(id);
    }
}
