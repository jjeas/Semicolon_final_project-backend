package com.semicolon.backend.domain.auth.service;

import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {
    //이 클래스는 인증 과정에서 AuthenticationProvider 가 DB에 로그인 시도하는 유저가 있는지 확인하는 UserDetailsService를 구현함
    private final MemberRepository repository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("DB에서 {}으로 사용자 검색",username);
        Member member=repository.findByMemberLoginId(username).orElseThrow(() -> new UsernameNotFoundException("해당 로그인ID의 회원이 없음"));
        return User.builder()
                .username(member.getMemberLoginId())
                .password(member.getMemberPassword())
                .authorities(String.valueOf(member.getMemberRole()))
                .build();
    }
}
