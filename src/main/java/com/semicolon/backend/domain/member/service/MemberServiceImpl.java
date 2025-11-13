package com.semicolon.backend.domain.member.service;

import com.semicolon.backend.domain.member.dto.MemberDTO;
import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.member.repository.MemberRepository;
import com.semicolon.backend.domain.partner.dto.PartnerDTO;
import com.semicolon.backend.domain.partner.entity.PartnerStatus;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

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
        repository.save(mapper.map(memberDTO, Member.class));
    }

    @Override
    public PartnerDTO getPartnerStatus(Long memberId) {
        return repository.getPartnerStatus(memberId)
                .orElse(new PartnerDTO((PartnerStatus) null));
    }
}
