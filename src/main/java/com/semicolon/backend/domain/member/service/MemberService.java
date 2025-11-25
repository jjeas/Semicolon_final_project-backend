package com.semicolon.backend.domain.member.service;

import com.semicolon.backend.domain.auth.dto.FindIdRequestDTO;
import com.semicolon.backend.domain.member.dto.MemberDTO;
import com.semicolon.backend.domain.member.dto.PasswordChangeDTO;
import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.partner.dto.PartnerDTO;

public interface MemberService {
    public void modify(String loginIdFromToken, MemberDTO requestDTO);
    public PartnerDTO getPartnerStatus(String loginIdFromToken);
    public MemberDTO getOneByLoginId(String loginId);
    public void changePassword(String loginIdFromToken,PasswordChangeDTO passwordChangeDTO);
}
