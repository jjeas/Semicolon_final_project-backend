package com.semicolon.backend.domain.auth.service;

import com.semicolon.backend.domain.auth.dto.FindIdRequestDTO;
import com.semicolon.backend.domain.auth.dto.FindPwRequestDTO;
import com.semicolon.backend.domain.auth.dto.LoginResponse;
import com.semicolon.backend.domain.auth.dto.ResetPasswordDTO;
import com.semicolon.backend.domain.member.dto.MemberDTO;
import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.member.entity.MemberRole;
import com.semicolon.backend.domain.member.repository.MemberRepository;
import com.semicolon.backend.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
// 1. 기본적으로 읽기 전용으로 설정 (성능 최적화)
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService{
    private final AuthenticationManager authenticationManager;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(String id, String password) {
        log.info("로그인 서비스 실행");
        UsernamePasswordAuthenticationToken beforeAuthentication = new UsernamePasswordAuthenticationToken(id, password);
        //사용자가 입력한 아이디, 패스워드로 UsernamePasswordAuthenticationToken 객체 생성
        Authentication auth = authenticationManager.authenticate(beforeAuthentication);
        //인증을 위해 필요한 아이디, 패스워드로 만들어진 beforeAuthentication 를 AuthenticationManager 에게 객체 형태로 전달해줌
        log.info("로그인 인증 성공");
        String authenticatedId=auth.getName(); // 인증된 로그인 아이디
        log.info("인증된 아이디: {} ", authenticatedId);
        Member member = memberRepository.findByMemberLoginId(authenticatedId).orElseThrow(() -> new NoSuchElementException("회원 정보를 찾을 수 없습니다."));
        //인증된 로그인 아이디로 DB에서 멤버를 찾는다
        Map<String, Object> claim = Map.of(
                "memberId", member.getMemberId(),
                "memberRole", member.getMemberRole(),
                "loginId", member.getMemberLoginId()
        ); //실제 유저 정보가 담긴 클레임을 만든다. 이 클레임을 바탕으로 토큰이 생성됨
        String accessToken = JwtUtil.generateToken(claim,1);
        //클레임 정보를 바탕으로 JWT 를 만들고 유효기간은 1시간으로 설정
        log.info("생성된 토큰={}",accessToken);
        return LoginResponse.builder()
                .loginId(member.getMemberLoginId())
                .accessToken(accessToken)
                .memberId(member.getMemberId())
                .memberRole(member.getMemberRole())
                .build(); // 토큰, 멤버 아이디, 멤버롤을 프론트에 보내준다
    }

    @Override
    @Transactional
    public void register(MemberDTO dto) {
        if(memberRepository.existsByMemberLoginId(dto.getMemberLoginId())){
            throw new IllegalArgumentException("이미 사용중인 아이디입니다.");
        }
        if(memberRepository.existsByMemberEmail(dto.getMemberEmail())){
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }
        if (dto.getPasswordConfirm() != null && !dto.getMemberPassword().equals(dto.getPasswordConfirm())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        String encodedPassword = passwordEncoder.encode(dto.getMemberPassword());
        Member newMember = Member.builder()
                .memberLoginId(dto.getMemberLoginId())
                .memberPassword(encodedPassword)
                .memberEmail(dto.getMemberEmail())
                .memberAddress(dto.getMemberAddress())
                .memberDetailAddress(dto.getMemberDetailAddress())
                .memberGender(dto.getMemberGender())
                .socialLogin("LOCAL")
                .memberJoinDate(LocalDateTime.now())
                .memberBirthDate(dto.getMemberBirthDate())
                .memberPhoneNumber(dto.getMemberPhoneNumber())
                .memberName(dto.getMemberName())
                .memberRole(MemberRole.ROLE_USER)
                .build();
        memberRepository.save(newMember);
    }
    @Override
    public String findMemberId(FindIdRequestDTO dto) {
        Optional<Member> foundMember = memberRepository.findMemberIdByMemberNameAndMemberEmail(dto.getMemberName(),dto.getMemberEmail());
        if(foundMember.isPresent()) {
            log.info("아이디 찾기 완료 아이디={}",foundMember.get().getMemberLoginId());
            return maskingId(foundMember.get().getMemberLoginId());
        }
        else {
            throw new NoSuchElementException("사용자를 찾을 수 없습니다.");
        }
    }

    @Override
    public Boolean findMemberPw(FindPwRequestDTO dto) {
        Optional<Member> foundMember = memberRepository.findMemberByMemberNameAndMemberEmailAndMemberLoginId(
                dto.getMemberName(),dto.getMemberEmail(),dto.getMemberLoginId()
        );
        if(foundMember.isPresent()) {
            return true;
        }else{
            return false;
        }
    }

    @Override
    @Transactional
    public void changePassword(ResetPasswordDTO dto) {
        Member foundMember = memberRepository.findByMemberLoginId(dto.getMemberLoginId()).orElseThrow();
        String newPassword = passwordEncoder.encode(dto.getNewPassword());
        foundMember.setMemberPassword(newPassword);
    }

    @Override
    public boolean checkEmailDuplicate(String memberEmail) {
        return memberRepository.existsByMemberEmail(memberEmail);
    }

    @Override
    public boolean checkLoginIdDuplicate(String memberLoginId) {
        return memberRepository.existsByMemberLoginId(memberLoginId);
    }

    public String maskingId(String originalId){
        if(originalId.length()<=3){
            return originalId.substring(0,originalId.length()-1)+"*";
        }else{
            return originalId.substring(0,originalId.length()-3)+"***";
        }
    }
}
