package com.semicolon.backend.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Component // spring 이 이 클래스 자체를 Bean 으로 만들도록 하는 어노테이션
public class JwtCheckFilter extends OncePerRequestFilter { //요청 한번당 한번만 실행되도록 보장하는 클래스를 상속받음
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI(); // 컨트롤러 요청 URI
        if(uri.equals("/api/auth/login") || uri.equals("/api/auth/register")
            || uri.startsWith("/auth/") || uri.startsWith("/api/auth/")){ // 카카오 로그인 추가 코드 부분
            log.info("회원가입 or 로그인 시도 체인 즉시 통과");
            filterChain.doFilter(request,response); //회원가입 또는 로그인 시도 시 즉시 통과시킴(토큰이 없으니 유효성 검사 불가)
            return;
        }
        String header = request.getHeader("Authorization"); //Authorization 헤더에 담긴 내용을 꺼내옴
        if(header==null || !header.startsWith("Bearer ")) {
            log.info("토큰 유효성 검사 실패 헤더={} 프론트에서 온 정보={}",header,request );
            filterChain.doFilter(request,response); return;
            //유효성 검사 실패 시 체인을 돌지 않음
        }
        String jwtToken = header.substring(7); // Bearer  뒤에 오는 실제 토큰
        Map<String, Object> claim=null; //유저 정보가 담길 클레임 선언, 프론트에 반환해줘야 해서 맵형태로 생성
        try {
            claim = JwtUtil.validateToken(jwtToken); // 토큰 검증 try
            log.info("만들어진 클레임 정보={}",claim);
        }catch (Exception e){ //검증 중 오류 발생 시 실행
            log.info("--------토큰 에러 발생 {}--------",e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return; //에러 발생 시 인증 실패(401 에러) 반환 후 즉시 종료
        }
        if(claim==null) { //클레임이 비어있는 경우
            log.info("--------클레임 에러 발생--------");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return; //에러 발생 시 인증 실패(401 에러) 반환 후 즉시 종료
        }
        String loginId = (String)claim.get("loginId");
        String role = (String)claim.get("memberRole"); //클레임에는 실제 유저에 대한 정보가 담겨있고 이를 꺼내옴
        log.info("클레임에 담긴 정보 아이디={} 권한={}",loginId,role);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginId,null, List.of(new SimpleGrantedAuthority(role))); // <-- 이거땜에 @AuthenticationPrincipal 이 가능
        //UsernamePasswordAuthentication 은 AuthenticationManager 에 인증이 완료된 사용자의 정보를 전달해주는데
        //로그인 아이디와 권한을 전달해준다. credentials 는 패스워드인데 프론트에 전달될거라 패스워드를 담으면 안되기 때문에 null 로 전달
        SecurityContextHolder.getContext().setAuthentication(token);
        //시큐리티 콘텍스트 홀더에 인증된 사용자의 정보가 담긴 토큰을 전달해줘야 필터가 아 이 사용자는 인증된 사용자구나 하고 기억하게 된다
        log.info("콘텍스트홀더에 토큰 전달 완료");
        filterChain.doFilter(request,response);
        //모든 인증작업이 완료되어 다음 단계(컨트롤러)로 요청을 넘긴다
    }
}
