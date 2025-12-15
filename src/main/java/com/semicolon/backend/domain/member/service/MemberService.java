package com.semicolon.backend.domain.member.service;

import com.semicolon.backend.domain.auth.dto.FindIdRequestDTO;
import com.semicolon.backend.domain.member.dto.MemberDTO;
import com.semicolon.backend.domain.member.dto.PasswordChangeDTO;
import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.partner.dto.PartnerDTO;
import com.semicolon.backend.global.pageable.PageRequestDTO;
import com.semicolon.backend.global.pageable.PageResponseDTO;

import java.util.List;

public interface MemberService {
    public void modify(String loginIdFromToken, MemberDTO requestDTO);
    public PartnerDTO getPartnerStatus(String loginIdFromToken);
    public MemberDTO getOneByLoginId(String loginId);
    public void changePassword(String loginIdFromToken,PasswordChangeDTO passwordChangeDTO);
    public void modifyByAdmin(MemberDTO requestDTO);
    public PageResponseDTO<MemberDTO> searchMembers(PageRequestDTO pageRequestDTO);
    public List<MemberDTO> adminSearchMembers(String keyword);
}
