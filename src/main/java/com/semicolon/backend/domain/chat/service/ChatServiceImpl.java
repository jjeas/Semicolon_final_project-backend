package com.semicolon.backend.domain.chat.service;

import com.semicolon.backend.domain.chat.dto.ChatDTO;
import com.semicolon.backend.domain.chat.dto.ChatResDTO;
import com.semicolon.backend.domain.chat.entity.Chat;
import com.semicolon.backend.domain.chat.repository.ChatRepository;
import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService{

    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void saveMessage(ChatDTO dto, String loginId) {
        Member sender = memberRepository.findByMemberLoginId(loginId).orElseThrow(()->new IllegalArgumentException("해당하는 회원 없음!"));
        Chat chat = Chat.builder()
                .sender(sender)
                .message(dto.getMessage())
                .isRead(sender.getMemberRole().name().equals("ROLE_ADMIN"))
                .roomId(dto.getRoomId())
                .sendAt(LocalDateTime.now())
                .build();
        chatRepository.save(chat);
    }

    @Override
    public List<ChatResDTO> getRoomList() {
        List<Chat> chats = chatRepository.findRoomList();
        return chats.stream().map(chat->{
            boolean isAdmin = chat.getSender().getMemberRole().name().equals("ROLE_ADMIN");
            ChatResDTO dto = ChatResDTO.builder()
                .roomId(chat.getRoomId())
                .isReplied(isAdmin)
                .lastMessage(chat.getMessage())
                .lastSendAt(chat.getSendAt())
                .memberName(chat.getSender().getMemberLoginId())
                .build();
            return dto;
        }).toList();
    }

    @Override
    public List<ChatDTO> getChatHistory(Long roomId) {
        return chatRepository.findByRoomIdOrderBySendAtAsc(roomId).stream().map(chat -> ChatDTO.builder()
                .roomId(chat.getRoomId())
                .sender(chat.getSender().getMemberLoginId())
                .message(chat.getMessage())
                .build()
        ).toList();
    }
}
