package com.semicolon.backend.domain.member.service;

import com.semicolon.backend.domain.member.dto.MemberDTO;
import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.partner.dto.PartnerDTO;

import java.util.Optional;

public interface MemberService {
    public MemberDTO getOne(Long memberId);
    public void register(MemberDTO memberDTO);
    public PartnerDTO getPartnerStatus(Long memberId);
}
