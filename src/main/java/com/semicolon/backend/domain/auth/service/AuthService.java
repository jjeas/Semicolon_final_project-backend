package com.semicolon.backend.domain.auth.service;

import com.semicolon.backend.domain.auth.dto.*;
import com.semicolon.backend.domain.member.dto.MemberDTO;

public interface AuthService {
    LoginResponse login(String id, String password);
    void register(MemberDTO dto);
    String findMemberId(FindIdRequestDTO dto);
    Boolean findMemberPw(FindPwRequestDTO dto);
    void changePassword(ResetPasswordDTO dto);
    boolean checkEmailDuplicate(String memberEmail);
    boolean checkLoginIdDuplicate(String memberLoginId);
}
