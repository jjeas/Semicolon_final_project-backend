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
public class NaverAuthServiceImpl implements NaverAuthService {

    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${naver.client-id}")
    private String naverClientId;
    @Value("${naver.client-secret}")
    private String naverClientSecret;
    @Value("${naver.redirect-uri}")
    private String naverRedirectUri;

    @Override
    public TokenResponseDTO naverLogin(String code) {

        String naverAccessToken = getNaverAccessToken(code);
        NaverUser naverUser = getNaverUser(naverAccessToken);
        Member member = memberRepository
                .findByMemberLoginId(naverUser.loginId())
                .orElseGet(() -> createNaverMember(naverUser));

        // JWT 생성 (기존 로직 그대로)
        Map<String, Object> claim = Map.of(
                "memberId", member.getMemberId(),
                "memberRole", member.getMemberRole(),
                "loginId", member.getMemberLoginId()
        );

        String jwt = JwtUtil.generateToken(claim, 1);

        log.info("네이버 로그인 성공 loginId={}", member.getMemberLoginId());

        return new TokenResponseDTO(
                jwt,
                member.getMemberRole().name(),
                member.getMemberLoginId()
        );
    }

    // ================= 내부 메서드 =================
    private String getNaverAccessToken(String code) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", naverClientId);
        params.add("client_secret", naverClientSecret);
        params.add("redirect_uri", naverRedirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://nid.naver.com/oauth2.0/token",
                request,
                Map.class
        );

        if (response.getBody() == null || response.getBody().get("access_token") == null) {
            throw new IllegalStateException("네이버 access_token 발급 실패");
        }

        return response.getBody().get("access_token").toString();
    }

    private NaverUser getNaverUser(String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Map.class
        );

        if (response.getBody() == null) {
            throw new IllegalStateException("네이버 사용자 정보 조회 실패");
        }

        Map<String, Object> body = response.getBody();
        Map<String, Object> responseMap =
                (Map<String, Object>) body.get("response");

        if (responseMap == null) {
            throw new IllegalStateException("네이버 response 없음");
        }

        // 네이버 고유 ID
        String naverId = responseMap.get("id").toString();
        String loginId = "naver_" + naverId;

        // 이름
        String name = (String) responseMap.getOrDefault("name", "네이버회원");

        // 이메일
        String email = (String) responseMap.getOrDefault(
                "email", loginId + "@naver.com"
        );

        // 성별 (M / F / U)
        String gender = (String) responseMap.getOrDefault("gender", "U");

        // 생년월일 (yyyy-MM-dd or MM-dd)
        LocalDate birthDate = LocalDate.of(1900, 1, 1);
        if (responseMap.get("birthyear") != null && responseMap.get("birthday") != null) {
            int year = Integer.parseInt(responseMap.get("birthyear").toString());
            String birthday = responseMap.get("birthday").toString(); // MM-dd
            String[] parts = birthday.split("-");
            birthDate = LocalDate.of(
                    year,
                    Integer.parseInt(parts[0]),
                    Integer.parseInt(parts[1])
            );
        }

        // 전화번호
        String phoneNumber =
                (String) responseMap.getOrDefault("mobile", "000-0000-0000");

        // 주소 (네이버는 기본 제공 안 함)
        String address = "NAVER";

        return new NaverUser(
                loginId,
                name,
                email,
                gender,
                birthDate,
                phoneNumber,
                address
        );
    }

    private Member createNaverMember(NaverUser user) {

        return memberRepository.save(
                Member.builder()
                        .memberLoginId(user.loginId())
                        .memberPassword("SOCIAL_LOGIN")
                        .memberName(user.name())
                        .memberEmail(user.email())
                        .memberGender(user.gender())
                        .socialLogin("NAVER")
                        .memberBirthDate(user.birthDate())
                        .memberPhoneNumber(user.phoneNumber())
                        .memberAddress(user.address())
                        .memberJoinDate(LocalDateTime.now())
                        .memberRole(MemberRole.ROLE_USER)
                        .build()
        );
    }

    // 내부 record
    private record NaverUser(
            String loginId,
            String name,
            String email,
            String gender,
            LocalDate birthDate,
            String phoneNumber,
            String address
    ) {}
}
