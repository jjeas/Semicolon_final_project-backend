package com.semicolon.backend.domain.support.service;

import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.member.repository.MemberRepository;
import com.semicolon.backend.domain.support.dto.SupportDTO;
import com.semicolon.backend.domain.support.dto.SupportUploadDTO;
import com.semicolon.backend.domain.support.entity.Support;
import com.semicolon.backend.domain.support.entity.SupportFile;
import com.semicolon.backend.domain.support.entity.SupportStatus;
import com.semicolon.backend.domain.support.repository.SupportFileRepository;
import com.semicolon.backend.domain.support.repository.SupportRepository;
import com.semicolon.backend.global.file.uploadFile.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupportServiceImpl implements SupportService {

    private final MemberRepository memberRepository;
    private final CustomFileUtil customFileUtil;
    private final SupportRepository supportRepository;
    private final SupportFileRepository supportFileRepository;

    @Override
    public ResponseEntity<?> supportReqRegister(SupportDTO supportDTO) {
        Member member = memberRepository.findById(supportDTO.getMemberId()).orElseThrow();

        Support support = new Support();
        support.setMember(member);
        support.setCreatedDate(LocalDateTime.now());
        support.setStatus(SupportStatus.WAITING);
        support.setTitle(supportDTO.getSupportTitle());
        support.setContent(supportDTO.getSupportContent());
        supportRepository.save(support);

        log.info("supportRepository => {}", supportRepository);

        List<String> fileNames = customFileUtil.saveFiles(supportDTO.getSupportFiles(), "supportFiles");

        if(fileNames != null) {
            for(var i=0; i<fileNames.size(); i += 2){
                String originalName = fileNames.get(i);
                String savedName = fileNames.get(i+1);

                SupportFile supportFile = new SupportFile();
                supportFile.setOriginalName(originalName);
                supportFile.setSavedName(savedName);
                supportFile.setFilePath(customFileUtil.getUploadPath() + "/supportFiles/" + savedName);
                supportFile.setUploadDate(LocalDateTime.now());

                support.addSupport(supportFile);
                supportFileRepository.save(supportFile);
                log.info("supportFileRepository => {}", supportFileRepository);
            }
        }
        log.info("문의 등록 완료 => {}{}{}{}{}", support.getStatus(), support.getTitle(), support.getContent(), support.getFiles(), support.getStatus());
        return ResponseEntity.ok("문의 등록 완료");
    }

    @Override
    public List<SupportUploadDTO> getSupportList(Long id) {

        List<Support> supportList = supportRepository.findAll().stream()
                .filter(i -> i.getMember().getMemberId() == id)
                .toList();

        return supportList.stream()
                .map(i -> {
            List<String> fileNames = i.getFiles().stream()
                    .map(j->j.getOriginalName()).toList();
            List<String> filePaths = i.getFiles().stream()
                    .map(j->j.getFilePath()).toList();
            List<String> savedName = i.getFiles().stream()
                    .map(j->j.getSavedName()).toList();

            return SupportUploadDTO.builder()
                    .supportNo(i.getSupportNo())
                    .status(i.getStatus().name())
                    .memberId(i.getMember().getMemberId())
                    .supportTitle(i.getTitle())
                    .supportContent(i.getContent())
                    .fileName(fileNames)
                    .filePath(filePaths)
                    .savedName(savedName)
                    .createdDate(i.getCreatedDate())
                    .build();
        }).toList();
    }

    @Override
    public SupportUploadDTO getOneSupport(Long id, Long no) {

       Support support = supportRepository.findById(no).orElseThrow();

        List<String> fileNames= support.getFiles().stream().map(i->i.getOriginalName()).toList();
        List<String> filePath = support.getFiles().stream().map(i->i.getFilePath()).toList();
        List<String> savedName = support.getFiles().stream().map(i->i.getSavedName()).toList();

        return SupportUploadDTO.builder()
                .supportNo(support.getSupportNo())
                .status(support.getStatus().name())
                .memberId(support.getMember().getMemberId())
                .supportContent(support.getContent())
                .supportTitle(support.getTitle())
                .filePath(filePath)
                .fileName(fileNames)
                .savedName(savedName)
                .createdDate(support.getCreatedDate())
                .build();
    }
}
