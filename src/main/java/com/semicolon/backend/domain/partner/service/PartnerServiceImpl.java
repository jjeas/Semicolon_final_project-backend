package com.semicolon.backend.domain.partner.service;

import com.semicolon.backend.domain.member.dto.MemberDTO;
import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.member.entity.MemberRole;
import com.semicolon.backend.domain.member.repository.MemberRepository;
import com.semicolon.backend.domain.partner.dto.PartnerDTO;
import com.semicolon.backend.domain.partner.dto.PartnerFileDTO;
import com.semicolon.backend.domain.partner.dto.PartnerUploadDTO;
import com.semicolon.backend.domain.partner.entity.Partner;
import com.semicolon.backend.domain.partner.entity.PartnerFile;
import com.semicolon.backend.domain.partner.entity.PartnerStatus;
import com.semicolon.backend.domain.partner.repository.PartnerFileRepository;
import com.semicolon.backend.domain.partner.repository.PartnerRepository;
import com.semicolon.backend.global.file.uploadFile.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper mapper;

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
            for (int i = 0; i < resumeNames.size(); i += 2) {
                String originalName = resumeNames.get(i);
                String savedName = resumeNames.get(i + 1);

                PartnerFile partnerFile = new PartnerFile();
                partnerFile.setOriginalName(originalName);
                partnerFile.setSavedName(savedName);
                partnerFile.setFileCategory("resume");
                partnerFile.setPartner(partner);
                partnerFile.setFilePath(customFileUtil.getUploadPath() + "/resume/" + savedName);

                partner.addPartner(partnerFile);
                partnerFileRepository.save(partnerFile);
                log.info("2) 여기 들어오는지 확인 => {}", partnerFile);
            }
        }

        if (certNames != null) {
            for (int i = 0; i < certNames.size(); i += 2) {
                String originalName = certNames.get(i);
                String savedName = certNames.get(i + 1);

                PartnerFile partnerFile = new PartnerFile();
                partnerFile.setOriginalName(originalName);
                partnerFile.setSavedName(savedName);
                partnerFile.setFileCategory("cert");
                partnerFile.setPartner(partner);
                partnerFile.setFilePath(customFileUtil.getUploadPath() + "/cert/" + savedName);

                partner.addPartner(partnerFile);
                partnerFileRepository.save(partnerFile);
            }
        }

        if (bankNames != null) {
            for (int i = 0; i < bankNames.size(); i += 2) {
                String originalName = bankNames.get(i);
                String savedName = bankNames.get(i + 1);

                PartnerFile partnerFile = new PartnerFile();
                partnerFile.setOriginalName(originalName);
                partnerFile.setSavedName(savedName);
                partnerFile.setFileCategory("bank");
                partnerFile.setPartner(partner);
                partnerFile.setFilePath(customFileUtil.getUploadPath() + "/bank/" + savedName);

                partner.addPartner(partnerFile);
                partnerFileRepository.save(partnerFile);
            }
        }
        log.info("파트너 신청 완료 => {}{}{}", partner.getMember(), partner.getFiles(), partner.getPartnerClass());

        return ResponseEntity.ok("파트너 신청 완료");
    }

    @Override
    public List<PartnerUploadDTO> getList() {
        List<Partner> list = partnerRepository.findAll();
        List<PartnerUploadDTO> uploadDTOList = list.stream().map(i->entityToDto(i)).toList();
        return uploadDTOList;
    }

    @Override
    public PartnerUploadDTO getOne(Long id) {
        Partner partner = partnerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("파트너 승급 신청서를 찾을 수 없습니다."));
        return entityToDto(partner);
    }

    @Override
    @Transactional
    public void changeStatus(Long id, PartnerStatus status) {
        Partner partner = partnerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("파트너 승급 신청서를 찾을 수 없습니다."));
        if(status == PartnerStatus.ACCEPTED){
            Member member = memberRepository.findById(partner.getMember().getMemberId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 멤버를 찾을 수 없습니다."));
            member.setMemberRole(MemberRole.PARTNER);
            memberRepository.save(member);
        }
        partner.setStatus(status);
        partnerRepository.save(partner);
    }

    public PartnerUploadDTO entityToDto(Partner partner){
        List<PartnerFileDTO> resume = partner.getFiles()
                .stream().filter(i->i.getFileCategory().equals("resume"))
                .map(j->PartnerFileDTO.builder()
                        .originalName(j.getOriginalName())
                        .savedName(j.getSavedName())
                        .filePath(j.getFilePath())
                        .fileId(j.getFileId())
                        .fileCategory(j.getFileCategory())
                        .build())
                .toList();

        List<PartnerFileDTO> bank = partner.getFiles()
                .stream().filter(i->i.getFileCategory().equals("bank"))
                .map(j->PartnerFileDTO.builder()
                        .originalName(j.getOriginalName())
                        .savedName(j.getSavedName())
                        .filePath(j.getFilePath())
                        .fileId(j.getFileId())
                        .fileCategory(j.getFileCategory())
                        .build())
                .toList();

        List<PartnerFileDTO> cert = partner.getFiles()
                .stream().filter(i->i.getFileCategory().equals("cert"))
                .map(j->PartnerFileDTO.builder()
                        .originalName(j.getOriginalName())
                        .savedName(j.getSavedName())
                        .filePath(j.getFilePath())
                        .fileId(j.getFileId())
                        .fileCategory(j.getFileCategory())
                        .build())
                .toList();

        return PartnerUploadDTO.builder()
                .requestNo(partner.getRequestNo())
                .bankFiles(bank)
                .certFiles(cert)
                .resumeFiles(resume)
                .member(mapper.map(partner.getMember(), MemberDTO.class))
                .status(partner.getStatus().toString())
                .partnerClass(partner.getPartnerClass())
                .requestDate(partner.getRequestDate())
                .build();
    }
}

