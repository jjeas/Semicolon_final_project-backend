package com.semicolon.backend.domain.chat.service;

import com.semicolon.backend.domain.chat.dto.ChatDTO;
import com.semicolon.backend.domain.chat.dto.ChatResDTO;
import com.semicolon.backend.domain.chat.entity.Chat;
import com.semicolon.backend.domain.chat.repository.ChatRepository;
import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.member.entity.MemberRole;
import com.semicolon.backend.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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

        return chats.stream().map(chat -> {
            String displaySenderId = chat.getSender().getMemberLoginId(); // 기본값: 보낸 사람
            boolean isAdmin = chat.getSender().getMemberRole().name().equals("ROLE_ADMIN");

            // 관리자가 보낸 경우 -> 진짜 유저 ID 찾기
            if (isAdmin) {
                // PageRequest.of(0, 1) -> "0페이지에서 1개만 줘" (LIMIT 1 효과)
                List<String> userIds = chatRepository.findUserLoginId(
                        chat.getRoomId(),
                        MemberRole.ROLE_ADMIN,
                        PageRequest.of(0, 1)
                );

                // 찾은 유저가 있으면 덮어쓰기
                if (!userIds.isEmpty()) {
                    displaySenderId = userIds.get(0);
                }
            }

            // 프론트로 나가는 건 기존과 똑같은 ChatResDTO 입니다.
            return ChatResDTO.builder()
                    .roomId(chat.getRoomId())
                    .senderId(displaySenderId) // 여기에 유저 ID가 들어감
                    .lastMessage(chat.getMessage())
                    .lastSendAt(chat.getSendAt())
                    .senderRole(chat.getSender().getMemberRole())
                    .build();
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
