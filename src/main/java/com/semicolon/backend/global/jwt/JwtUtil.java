package com.semicolon.backend.global.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

@Component
@Slf4j
public class JwtUtil {
    private static final String SECRET_KEY="CWRi279a5Jyuwypm9kzotUaKpnLQHypXVg5xnOlJKL0";
    private static final Key singingKey;
    static {
        singingKey = new SecretKeySpec(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8),
                SignatureAlgorithm.HS256.getJcaName());
    }

    public static String generateToken(Map<String, Object> valueMap, int hours){ //토큰생성 로직
        ZonedDateTime now = ZonedDateTime.now();
        Date expireDate = Date.from(now.plusHours(hours).toInstant()); //현재시간에서 인자로 받은 hours 만큼 토큰 유효시간 설정
        String jwtStr = Jwts.builder()
                .setHeaderParam("typ","JWT") //반환해줄 토큰 해더 설정
                .setClaims(valueMap)//valueMap을 클레임으로 설정
                .setIssuedAt(new Date())//생성일 설정(생성시점 현재시간)
                .setExpiration(expireDate)//만료일 설정
                .signWith(singingKey)//signingKey 로 시그니처 생성
                .compact();//위 과정에서 만들어진 헤더 페이로드 시그니처를 합침
        log.info("토큰 생성 완료");
        return jwtStr;
    }
    public static Map<String, Object> validateToken(String token){
        Claims claim=null;
        try {
            claim = Jwts.parserBuilder() //jwt 분해기 생성
                    .setSigningKey(singingKey)//singingKey를 기준으로 토큰의 시그니처를 해석하도록 설정
                    .build()//생성 완료
                    .parseClaimsJws(token)//생성 후 즉시 토큰 분해
                    .getBody();//분해된 원본 정보가 나온다
        }catch(ExpiredJwtException e){
            log.error("토큰 만료 에러 {}",e.getMessage());
            return null;
        } catch(SignatureException e){
            log.error("토큰 위조 에러 {}",e.getMessage());
            return null;
        } catch(Exception e){
            log.error("토큰 검증 에러 {}",e.getMessage());
        }
        return claim;
    }

}
