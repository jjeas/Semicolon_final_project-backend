package com.semicolon.backend.domain.chat.repository;

import com.semicolon.backend.domain.chat.entity.Chat;
import com.semicolon.backend.domain.member.entity.MemberRole;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface ChatRepository extends JpaRepository<Chat,Long> {

    @Query("select c from Chat c where (c.sendAt, c.roomId) in (select Max(sub.sendAt), sub.roomId from Chat sub group by sub.roomId) order by c.sendAt desc") //최신 메세지가 온 순서대로 방 번호만 가져오기
    List<Chat> findRoomList();
    List<Chat> findByRoomIdOrderBySendAtAsc(Long id);

    @Query("SELECT c.sender.memberLoginId FROM Chat c WHERE c.roomId = :roomId AND c.sender.memberRole <> :adminRole")
    List<String> findUserLoginId(@Param("roomId") Long roomId, @Param("adminRole") MemberRole adminRole, Pageable pageable);
}
