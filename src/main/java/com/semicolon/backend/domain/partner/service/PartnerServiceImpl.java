package com.semicolon.backend.domain.partner.service;

import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.member.repository.MemberRepository;
import com.semicolon.backend.domain.partner.dto.PartnerDTO;
import com.semicolon.backend.domain.partner.entity.Partner;
import com.semicolon.backend.domain.partner.entity.PartnerFile;
import com.semicolon.backend.domain.partner.entity.PartnerStatus;
import com.semicolon.backend.domain.partner.repository.PartnerFileRepository;
import com.semicolon.backend.domain.partner.repository.PartnerRepository;
import com.semicolon.backend.domain.partner.uploadFile.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class PartnerServiceImpl implements PartnerService{

    private final MemberRepository memberRepository;
    private final PartnerRepository partnerRepository;
    private final PartnerFileRepository partnerFileRepository;
    private final CustomFileUtil customFileUtil;

    @Override
    public ResponseEntity<?> requestPartnerForm(PartnerDTO dto) {
        Member member = memberRepository.findById(dto.getMemberId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. memberId=" + dto.getMemberId()));

        Partner partner = Partner.builder()
                .member(member)
                .requestDate(LocalDateTime.now())
                .status(PartnerStatus.PENDING)
                .partnerClass(dto.getPartnerClass())
                .build();

        log.info("1) 여기 들어오는지 확인 => {}", partner);
        partnerRepository.save(partner);

        List<String> resumeNames = customFileUtil.saveFiles(dto.getResumeFiles(), "resume");
        List<String> certNames = customFileUtil.saveFiles(dto.getCertFiles(), "cert");
        List<String> bankNames = customFileUtil.saveFiles(dto.getBankFiles(), "bank");

        if (resumeNames != null) {
            for (String name : resumeNames) {
                PartnerFile partnerFile = new PartnerFile();
                partnerFile.setSavedName(name);
                partnerFile.setFileCategory("resume");
                partnerFile.setPartner(partner);
                partnerFile.setFilePath(customFileUtil.getUploadPath() + "/" + partnerFile.getFileCategory() + "/" + name);

                partner.addPartner(partnerFile);
                partnerFileRepository.save(partnerFile);
                log.info("2) 여기 들어오는지 확인 => {}", partnerFile);
            }
        }

        if (certNames != null) {
            for (String name : certNames) {
                PartnerFile partnerFile = new PartnerFile();
                partnerFile.setSavedName(name);
                partnerFile.setFileCategory("cert");
                partnerFile.setPartner(partner);
                partnerFile.setFilePath(customFileUtil.getUploadPath() + "/" + partnerFile.getFileCategory() + "/" + name);

                partner.addPartner(partnerFile);
                partnerFileRepository.save(partnerFile);
            }
        }

        if (bankNames != null) {
            for (String name : bankNames) {
                PartnerFile partnerFile = new PartnerFile();
                partnerFile.setSavedName(name);
                partnerFile.setFileCategory("bank");
                partnerFile.setPartner(partner);
                partnerFile.setFilePath(customFileUtil.getUploadPath() + "/" + partnerFile.getFileCategory() + "/" + name);

                partner.addPartner(partnerFile);
                partnerFileRepository.save(partnerFile);
            }
        }
        log.info("partnerForm => {}{}{}", partner.getMember(), partner.getFiles(), partner.getPartnerClass());

        return ResponseEntity.ok("파트너 신청 완료");
    }
}

