package com.semicolon.backend.domain.member.service;

import com.semicolon.backend.domain.member.dto.MemberDTO;

public interface MemberService {
    public MemberDTO getOne(Long memberId);
    public void register(MemberDTO memberDTO);
}
