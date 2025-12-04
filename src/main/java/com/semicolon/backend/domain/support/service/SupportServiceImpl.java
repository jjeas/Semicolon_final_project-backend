package com.semicolon.backend.domain.support.service;

import com.semicolon.backend.domain.member.dto.MemberDTO;
import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.member.repository.MemberRepository;
import com.semicolon.backend.domain.schedule.dto.ScheduleDTO;
import com.semicolon.backend.domain.schedule.entity.Schedule;
import com.semicolon.backend.domain.support.dto.SupportDTO;
import com.semicolon.backend.domain.support.dto.SupportResponseDTO;
import com.semicolon.backend.domain.support.dto.SupportUploadDTO;
import com.semicolon.backend.domain.support.entity.Support;
import com.semicolon.backend.domain.support.entity.SupportFile;
import com.semicolon.backend.domain.support.entity.SupportResponse;
import com.semicolon.backend.domain.support.entity.SupportStatus;
import com.semicolon.backend.domain.support.repository.SupportFileRepository;
import com.semicolon.backend.domain.support.repository.SupportRepository;
import com.semicolon.backend.global.file.uploadFile.CustomFileUtil;
import com.semicolon.backend.global.pageable.PageRequestDTO;
import com.semicolon.backend.global.pageable.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final ModelMapper mapper;

    @Override
    public ResponseEntity<?> supportReqRegister(String loginIdFromToken, SupportDTO supportDTO) {
        log.info("이거이거이거이거 = {}",supportDTO);
        Long id = memberRepository.findByMemberLoginId(loginIdFromToken).get().getMemberId();
        Member member = memberRepository.findById(id).orElseThrow();

        Support support = new Support();
        support.setMember(member);
        support.setCreatedDate(LocalDateTime.now());
        support.setStatus(SupportStatus.WAITING);
        support.setSupportTitle(supportDTO.getSupportTitle());
        support.setSupportContent(supportDTO.getSupportContent());
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
        log.info("문의 등록 완료 => {}{}{}{}{}", support.getStatus(), support.getSupportTitle(), support.getSupportContent(), support.getFiles(), support.getStatus());
        return ResponseEntity.ok("문의 등록 완료");
    }

    @Override
    public List<SupportUploadDTO> getSupportList(Long id) {

        List<Support> supportList = supportRepository.findAll().stream()
                .filter(i -> i.getMember().getMemberId() == id)
                .toList();

        return supportList.stream()
                .map(this::entityToDTO).toList();
    }

    @Override
    public SupportUploadDTO getOneSupport(Long no) {

       Support support = supportRepository.findDetailWithResponse(no).orElseThrow();

        return entityToDTO(support);
    }

    @Override
    public PageResponseDTO<SupportUploadDTO> getSupportAllList(PageRequestDTO pageRequestDTO) {

        Sort sort = Sort.by(Sort.Order.desc("status"), Sort.Order.desc("createdDate"));
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(), sort);

        String keyword = pageRequestDTO.getKeyword();
        String type = pageRequestDTO.getType();
        boolean isKeywordEmpty = (keyword == null || keyword.isBlank());

        Page<Support> resultPage;

        if (!isKeywordEmpty) {
            switch (type) {
                case "name":
                    resultPage = supportRepository.findByMemberMemberNameContains(keyword, pageable);
                    break;
                case "title":
                    resultPage = supportRepository.findBySupportTitleContains(keyword, pageable);
                    break;
                default:
                    resultPage = Page.empty(pageable);
            }
        }
        else {
            resultPage = supportRepository.findAllOrderByStatusAndDate(pageable);
        }

        List<SupportUploadDTO> dtoList = resultPage.getContent()
                .stream()
                .map(i -> entityToDTO(i))
                .toList();

        return PageResponseDTO.<SupportUploadDTO>withAll()
                .dtoList(dtoList)
                .pageRequestDTO(pageRequestDTO)
                .totalCnt(resultPage.getTotalElements())
                .build();
    }

    @Override
    public List<SupportResponseDTO> registerResponse(Long no, SupportResponseDTO dto) {
        Support support = supportRepository.findById(no)
                .orElseThrow(() -> new RuntimeException("문의 내역을 찾을 수 없습니다."));

        SupportResponse response = SupportResponse.builder()
                .content(dto.getContent())
                .createdAt(LocalDateTime.now())
                .support(support)
                .build();

        support.addResponse(response);
        support.setStatus(SupportStatus.ANSWERED);
        supportRepository.save(support);

        return support.getResponse().stream()
                .map(r -> SupportResponseDTO.builder()
                        .content(r.getContent())
                        .createdAt(r.getCreatedAt())
                        .build())
                .toList();
    }

    public SupportUploadDTO entityToDTO(Support support){
        List<String> fileNames = support.getFiles().stream().map(SupportFile::getOriginalName).toList();
        List<String> filePath = support.getFiles().stream().map(SupportFile::getFilePath).toList();
        List<String> savedName = support.getFiles().stream().map(SupportFile::getSavedName).toList();

        List<SupportResponseDTO> responses = support.getResponse().stream()
                .map(r -> SupportResponseDTO.builder()
                        .content(r.getContent())
                        .createdAt(r.getCreatedAt())
                        .build())
                .toList();

        return SupportUploadDTO.builder()
                .supportNo(support.getSupportNo())
                .status(support.getStatus().name())
                .member(mapper.map(support.getMember(), MemberDTO.class))
                .supportContent(support.getSupportContent())
                .supportTitle(support.getSupportTitle())
                .filePath(filePath)
                .fileName(fileNames)
                .savedName(savedName)
                .createdDate(support.getCreatedDate())
                .response(responses)
                .build();
    }

}
