package com.semicolon.backend.global.jwt;

import java.security.SecureRandom;
import java.util.Base64;

public class KeyGenerator {

    public static void main(String[] args) {
        //시그니처 알고리즘용 키 생성을 위한 클래스(크게 신경 x)
        // HS256 알고리즘(256비트)을 사용하려면 32바이트가 필요합니다.
        // HS512 알고리즘(512비트)을 사용하려면 64바이트가 필요합니다.

        int keyLengthBytes = 32; // 👈 HS256 사용 시 32
        // int keyLengthBytes = 64; // 👈 HS512 사용 시 64

        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[keyLengthBytes];
        random.nextBytes(keyBytes);

        String base64Key = Base64.getEncoder().encodeToString(keyBytes);

        System.out.println("--- 생성된 랜덤 Base64 키 ---");
        System.out.println(base64Key);
        System.out.println("---------------------------------");
    }
}
