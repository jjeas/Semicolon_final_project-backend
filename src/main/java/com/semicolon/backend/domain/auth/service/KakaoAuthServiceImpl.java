package com.semicolon.backend.domain.auth.service;

import com.semicolon.backend.domain.auth.dto.TokenResponseDTO;
import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.member.entity.MemberRole;
import com.semicolon.backend.domain.member.repository.MemberRepository;
import com.semicolon.backend.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoAuthServiceImpl implements KakaoAuthService {

    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${kakao.client-id}")
    private String kakaoClientId;
    @Value("${kakao.client-secret}")
    private String kakaoClientSecret;
    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Override
    public TokenResponseDTO kakaoLogin(String code) {
        String kakaoAccessToken = getKakaoAccessToken(code);
        KakaoUser kakaoUser = getKakaoUser(kakaoAccessToken);
        Member member = memberRepository
                .findByMemberLoginId(kakaoUser.loginId())
                .orElseGet(() -> createKakaoMember(kakaoUser));

        // JWT 생성 (기존 로직 그대로)
        Map<String, Object> claim = Map.of(
                "memberId", member.getMemberId(),
                "memberRole", member.getMemberRole(),
                "loginId", member.getMemberLoginId()
        );

        String jwt = JwtUtil.generateToken(claim, 1);

        log.info("카카오 로그인 성공 loginId={}", member.getMemberLoginId());

        return new TokenResponseDTO(
                jwt,
                member.getMemberRole().name(),
                member.getMemberLoginId()
        );
    }

    // ================= 내부 메서드 =================
    private String getKakaoAccessToken(String code) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("client_secret", kakaoClientSecret);
        params.add("redirect_uri", kakaoRedirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://kauth.kakao.com/oauth/token",
                request,
                Map.class
        );

        if (response.getBody() == null || response.getBody().get("access_token") == null) {
            throw new IllegalStateException("카카오 access_token 발급 실패");
        }

        return response.getBody().get("access_token").toString();
    }

    private KakaoUser getKakaoUser(String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Map.class
        );

        if (response.getBody() == null) {
            throw new IllegalStateException("카카오 사용자 정보 조회 실패");
        }

        Map<String, Object> body = response.getBody();

        // 카카오 고유 ID
        Long kakaoId = Long.valueOf(body.get("id").toString());
        String loginId = "kakao_" + kakaoId;

        Map<String, Object> kakaoAccount =
                (Map<String, Object>) body.get("kakao_account");

        if (kakaoAccount == null) {
            throw new IllegalStateException("kakao_account 없음");
        }

        // 이름
        String name = (String) kakaoAccount.getOrDefault("name", "카카오회원");

        // 이메일
        String email = (String) kakaoAccount.getOrDefault(
                "email", loginId + "@kakao.com"
        );

        // 성별
        String gender = (String) kakaoAccount.getOrDefault("gender", "U");

        // 생년월일 (birthyear + birthday → LocalDate)
        LocalDate birthDate = LocalDate.of(1900, 1, 1);
        if (kakaoAccount.get("birthyear") != null && kakaoAccount.get("birthday") != null) {
            int year = Integer.parseInt(kakaoAccount.get("birthyear").toString());
            String birthday = kakaoAccount.get("birthday").toString(); // MMdd
            int month = Integer.parseInt(birthday.substring(0, 2));
            int day = Integer.parseInt(birthday.substring(2, 4));
            birthDate = LocalDate.of(year, month, day);
        }

        // 전화번호
        String phoneNumber =
                (String) kakaoAccount.getOrDefault("phone_number", "000-0000-0000");

        // 주소
        String address =
                (String) kakaoAccount.getOrDefault("shipping_address", "KAKAO");

        return new KakaoUser(
                loginId,
                name,
                email,
                gender,
                birthDate,
                phoneNumber,
                address
        );
    }


    private Member createKakaoMember(KakaoUser user) {

        return memberRepository.save(
                Member.builder()
                        .memberLoginId(user.loginId())
                        .memberPassword("SOCIAL_LOGIN") // 소셜 로그인 표시용
                        .memberName(user.name())
                        .socialLogin("KAKAO")
                        .memberEmail(user.email())
                        .memberGender(user.gender())
                        .memberBirthDate(user.birthDate())
                        .memberPhoneNumber(user.phoneNumber())
                        .memberAddress(user.address())
                        .memberJoinDate(LocalDateTime.now())
                        .memberRole(MemberRole.ROLE_USER)
                        .build()
        );
    }

    // 내부 record
    private record KakaoUser(
            String loginId,
            String name,
            String email,
            String gender,
            LocalDate birthDate,
            String phoneNumber,
            String address
    ) {}
}
