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
public class StompHandler implements ChannelInterceptor { //웹소켓으로 메세지, 연결 등 시도시 체널인터셉터가 가로채서 확인

    private final UserDetailService userDetailService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel){ // 메세지가 서버로 가기 전 검사하는 메서드
        StompHeaderAccessor accessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        //웹소켓 메세지 내용을 확인하기 위한 도구를 가져옴
        if(StompCommand.CONNECT.equals(accessor.getCommand())){ // 채팅방 연결을 최초 시도하는 경우에만 검사하겠다
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            if(authHeader==null || !authHeader.startsWith("Bearer ")){ //헤더에 있는 토큰이 유효한지 검사
                throw new IllegalArgumentException("인증 헤더가 없거나 잘못됨"); //유효하지 않으면 던저버리기
            }
            String token = authHeader.substring(7); // 진짜 토큰 꺼내오기
            Map<String, Object> claims = JwtUtil.validateToken(token); // 토큰 인증 및 토큰에 담긴 유저 정보를 가져옴
            if(claims==null){ //유저정보 없으면 잘못된 토큰임
                log.error("토큰 인증 실패");
                throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
            }
            String loginId = (String) claims.get("loginId"); //유저의 로그인 아이디를 가져옴
            if(loginId==null){
                throw new IllegalArgumentException("토큰에 사용자 ID 가 없습니다.");
            }
            UserDetails userDetails = userDetailService.loadUserByUsername(loginId); //아이디로 유저를 DB에서 찾음
            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
            accessor.setUser(auth); // accessor 에 유저를 토큰 속 유저정보로 설정해줌 원래는 익명으로 되어있으니까
            log.info("웹소켓 연결 승인 user={}",loginId);
        }
        return message; //모든 검사가 끝나면 컨트롤러로 메세지를 보내줌
    }
}