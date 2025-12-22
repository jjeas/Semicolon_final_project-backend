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
import com.semicolon.backend.domain.support.dto.SupportUploadDTO;
import com.semicolon.backend.domain.support.entity.Support;
import com.semicolon.backend.global.file.uploadFile.CustomFileUtil;
import com.semicolon.backend.global.pageable.PageRequestDTO;
import com.semicolon.backend.global.pageable.PageResponseDTO;
import com.semicolon.backend.global.reservationFilter.ReservationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public void requestPartnerForm(Long id, PartnerDTO dto) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. memberId=" + id));

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
                partnerFile.setThumbnailPath(customFileUtil.getUploadPath() + "/resume/s_" + savedName);

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
                partnerFile.setThumbnailPath(customFileUtil.getUploadPath() + "/cert/s_" + savedName);

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
                partnerFile.setThumbnailPath(customFileUtil.getUploadPath() + "/bank/s_" + savedName);

                partner.addPartner(partnerFile);
                partnerFileRepository.save(partnerFile);
            }
        }
        log.info("파트너 신청 완료 => {}{}{}", partner.getMember(), partner.getFiles(), partner.getPartnerClass());
    }

    @Override
    public PageResponseDTO<PartnerUploadDTO> getList(PageRequestDTO pageRequestDTO) {

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("requestDate").descending()
        );

        Page<Partner> result;

        if (pageRequestDTO.getRole() == null || pageRequestDTO.getRole().isBlank()) {
            result = partnerRepository.findAll(pageable);
        }
        else {
            PartnerStatus status = PartnerStatus.valueOf(pageRequestDTO.getRole());
            result = partnerRepository.findByStatus(status, pageable);
        }

        List<PartnerUploadDTO> dtoList = result.getContent()
                .stream()
                .map(i->entityToDto(i))
                .toList();

        return PageResponseDTO.<PartnerUploadDTO>withAll()
                .dtoList(dtoList)
                .pageRequestDTO(pageRequestDTO)
                .totalCnt(result.getTotalElements())
                .build();
    }

    @Override
    public PartnerUploadDTO getOne(Long id) {
        Partner partner = partnerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("파트너 승급 신청서를 찾을 수 없습니다."));
        return entityToDto(partner);
    }

    @Override
    @Transactional
    public void changeStatus(Long id, PartnerStatus status) {

        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("파트너 승급 신청서를 찾을 수 없습니다."));

        switch (status) {

            case ACCEPTED -> {
                Member member = memberRepository.findById(partner.getMember().getMemberId())
                        .orElseThrow(() -> new IllegalArgumentException("해당 멤버를 찾을 수 없습니다."));
                member.setMemberRole(MemberRole.ROLE_PARTNER);
            }
            case REJECTED -> {
                partner.getFiles().forEach(i-> log.info("이게무엇엇일일까까까요요요??? => {}",i.getFilePath()));
                partner.getFiles().forEach(i->customFileUtil.deleteFile(i.getFilePath()));
                partner.getFiles().clear();
            }
        }
        partner.setStatus(status);
    }

    @Override
    public List<String> getPartnerClassList(String loginIdFromToken) {
        Member member = memberRepository.findByMemberLoginId(loginIdFromToken).orElseThrow(()-> new IllegalArgumentException("회원 정보가 없습니다"));
        Partner partner = partnerRepository.findByMember(member).orElseThrow(()-> new IllegalArgumentException("회원 정보가 없습니다"));

        return partner.getPartnerClass();
    }

    public PartnerUploadDTO entityToDto(Partner partner){
        List<PartnerFileDTO> resume = partner.getFiles()
                .stream().filter(i->i.getFileCategory().equals("resume"))
                .map(j->PartnerFileDTO.builder()
                        .originalName(j.getOriginalName())
                        .savedName(j.getSavedName())
                        .filePath(j.getFilePath())
                        .thumbnailPath(j.getThumbnailPath())
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
                        .thumbnailPath(j.getThumbnailPath())
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
                        .thumbnailPath(j.getThumbnailPath())
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

