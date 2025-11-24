package com.semicolon.backend.domain.member.service;

import com.semicolon.backend.domain.member.dto.MemberDTO;
import com.semicolon.backend.domain.member.dto.PasswordChangeDTO;
import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.member.repository.MemberRepository;
import com.semicolon.backend.domain.partner.dto.PartnerDTO;
import com.semicolon.backend.domain.partner.entity.PartnerStatus;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberRepository repository;
    @Autowired
    private ModelMapper mapper;

    @Override
    public MemberDTO getOne(Long memberId) {
        return mapper.map(repository.findById(memberId).orElseThrow(() -> new NoSuchElementException("해당 ID에 해당되는 회원이 없습니다.")), MemberDTO.class);
    }

    @Override
    public void register(MemberDTO memberDTO) {
        Member member = repository.findById(memberDTO.getMemberId())
                .orElseThrow(() -> new NoSuchElementException("해당 ID에 해당되는 회원이 없습니다."));


        member.setMemberEmail(memberDTO.getMemberEmail());
        member.setMemberPhoneNumber(memberDTO.getMemberPhoneNumber());
        member.setMemberAddress(memberDTO.getMemberAddress());

        repository.save(member);
    }

    @Override
    public PartnerDTO getPartnerStatus(Long memberId) {
        return repository.getPartnerStatus(memberId)
                .orElse(new PartnerDTO((PartnerStatus) null));
    }

    @Override
    public void changePassword(Long memberId, PasswordChangeDTO passwordChangeDTO) {
        Member member = repository.findById(memberId).orElseThrow(() -> new NoSuchElementException("해당 ID에 해당되는 회원이 없습니다."));

        if(!passwordChangeDTO.getMemberCurrentPassword().equals(member.getMemberPassword())) {
            throw new NoSuchElementException("기존 비밀번호가 일치하지 않습니다");
        }

        if(!passwordChangeDTO.getMemberPassword().equals(passwordChangeDTO.getMemberPasswordCheck())){
            throw new IllegalArgumentException("새 비밀번호 확인이 일치하지 않습니다");
        }

        member.setMemberPassword(passwordChangeDTO.getMemberPassword());
        repository.save(member);
    }


}
