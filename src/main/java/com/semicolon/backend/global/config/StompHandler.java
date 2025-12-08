package com.semicolon.backend.global.config;

import com.semicolon.backend.domain.auth.service.UserDetailService;
import com.semicolon.backend.global.jwt.JwtUtil; // 사용 중인 토큰 제공자
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final UserDetailService userDetailService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel){
        StompHeaderAccessor accessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if(StompCommand.CONNECT.equals(accessor.getCommand())){
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            if(authHeader==null || !authHeader.startsWith("Bearer ")){
                throw new IllegalArgumentException("인증 헤더가 없거나 잘못됨");
            }
            String token = authHeader.substring(7);
            Map<String, Object> claims = JwtUtil.validateToken(token);
            if(claims==null){
                log.error("토큰 인증 실패");
                throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
            }
            String loginId = (String) claims.get("loginId");
            if(loginId==null){
                throw new IllegalArgumentException("토큰에 사용자 ID 가 없습니다.");
            }
            UserDetails userDetails = userDetailService.loadUserByUsername(loginId);
            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
            accessor.setUser(auth);
            log.info("웹소켓 연결 승인 user={}",loginId);
        }
        return message;
    }
}