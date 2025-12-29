package com.semicolon.backend.global.config;

import com.semicolon.backend.global.jwt.JwtCheckFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.web.util.matcher.RegexRequestMatcher.regexMatcher;

@Configuration
@Slf4j
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtCheckFilter jwtCheckFilter;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    } //패스워드 엔코딩을 위해 빈 주입

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        log.info("--------필터체인 시작--------");
        httpSecurity
                .csrf(csrf->csrf.disable())
                .cors(Customizer.withDefaults())
                .formLogin(form->form.disable())
                .httpBasic(basic->basic.disable())
                //스프링 시큐리티의 기본 설정이 아닌 사용자 설정으로 사용하겠다
                .sessionManagement(session->session.sessionCreationPolicy((SessionCreationPolicy.STATELESS)))
                //쿠키 방식을 사용하기 때문에 세션 방식은 비활성화
                .authorizeHttpRequests(auth->auth
                .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()

                .requestMatchers("/api/auth/**").permitAll() // 카카오 로그인 추가 코드 부분
                .requestMatchers("/auth/**").permitAll()    // 카카오 로그인 추가 코드 부분

                //회원가입과 로그인에 대한 요청은 필터체인에서 제외한다(permitAll())
                .requestMatchers(HttpMethod.POST,"/api/community/notice/{id}/view").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/community/gallery/{id}/view").permitAll()
                .requestMatchers("/ws-chat/**").permitAll()
                //클릭시 조회수 1 증가 로직 허용
                .requestMatchers(HttpMethod.GET,"/api/community/**").permitAll()
                //커뮤니티 GET 로직 모두 허용
                .requestMatchers(HttpMethod.POST, "/api/member/findId").permitAll()
                //아이디 찾기 허용
                .requestMatchers("/api/auth/sendCode").permitAll()
                .requestMatchers("/api/auth/checkCode").permitAll()
                .requestMatchers("/api/auth/sendCodePw").permitAll()
                .requestMatchers("/api/auth/checkCodePw").permitAll()
                .requestMatchers("/api/auth/resetPassword").permitAll()
                //아이디, 비밀번호 찾기 허용
                .requestMatchers("/api/auth/check/**").permitAll()
                .requestMatchers("/api/program/**").permitAll()
                .requestMatchers("/upload/**", "/download/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/upload/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/gallery/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/guide/view/**").permitAll()
                .requestMatchers("/api/guide/**").permitAll()
                .requestMatchers("/api/lesson/check/**").permitAll()
//                .requestMatchers(HttpMethod.GET,"/upload/**").permitAll() 갤러리 등록을 위해 임시 주석 지우면 안됩니다!!
                .requestMatchers("/api/upload/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/community/gallery/**").permitAll() //                .requestMatchers("/api/community/gallery/**").permitAll()
                .requestMatchers(HttpMethod.PUT,"/api/community/gallery/**").permitAll()
                .requestMatchers("/upload/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/lesson/**").permitAll()
                //갤러리 이미지+콘텐트 조회 요청 허용
                .requestMatchers(HttpMethod.GET, "/api/program/**").permitAll()
                //프로그램 안내 조회 요청 허용
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                //options 방식 요청 모두 허용
                //여기까지 비회원 요청 가능한 링크 설정
                .requestMatchers(regexMatcher(".*admin.*")).hasRole("ADMIN")
                //관리자 컨트롤러에 대한 요청은 ROLE 이 ADMIN 인 경우에만 허용한다
                .requestMatchers(HttpMethod.POST, "/api/availableSpace/**").permitAll()
                .anyRequest().authenticated()
                //그 외의 모든 요청은 로그인된 사람만 가능
                );
        httpSecurity.addFilterBefore(jwtCheckFilter, UsernamePasswordAuthenticationFilter.class);
        //스프링 시큐리티의 기본 로그인 필터보다 커스텀한 JwtFilterCheck 를 먼저 쓰겠다(내가 만든거 쓰고싶다)
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
        //AuthenticationManager 를 공개적으로 사용하도록 Bean 주입
    }
}
