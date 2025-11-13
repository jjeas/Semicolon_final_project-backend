package com.semicolon.backend.domain.notice.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.semicolon.backend.domain.notice.dto.NoticeDTO;
import com.semicolon.backend.domain.notice.dto.NoticeFileDTO;
import com.semicolon.backend.domain.notice.entity.Notice;
import com.semicolon.backend.domain.notice.entity.NoticeFile;
import com.semicolon.backend.domain.notice.repository.NoticeRepository;
import com.semicolon.backend.global.file.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Not;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{

    private final NoticeRepository repository;
    private final FileUploadService service;
    private final ModelMapper mapper;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");

    public Notice toEntity(NoticeDTO dto){
        return Notice.builder()
                .content(dto.getContent())
                .title(dto.getTitle())
                .createdAt(LocalDateTime.now())
                .viewCount(0)
                .build();
    }
    public NoticeDTO toDto(Notice notice){
        return NoticeDTO.builder()
                .noticeId(notice.getNoticeId())
                .content(notice.getContent())
                .title(notice.getTitle())
                .createdAt(notice.getCreatedAt())
                .viewCount(notice.getViewCount())
                .build();
    }

    @Override
    public void deleteNotice(Long noticeId) {
        repository.deleteById(noticeId);
    }

    @Override
    public List<NoticeDTO> list() {
        return repository.findAll().stream().map(notice->toDto(notice)).toList();
    }

    @Override
    public void modify(Long id, NoticeDTO dto) {
        Notice notice = repository.findById(id).orElseThrow();
        notice.setContent(dto.getContent());
        notice.setTitle(dto.getTitle());
        repository.save(notice);
    }

    @Override
    public void increaseViewCount(Long id) {
        Notice notice = repository.findById(id).orElseThrow(()->new IllegalArgumentException("해당하는 공지사항이 없습니다."));
        notice.setViewCount(notice.getViewCount()+1);
        log.info("조회수 1 증가 공지={}",notice);
        repository.save(notice);
    }

    @Override
    public void registerNotice(NoticeDTO dto) {

        Notice notice = toEntity(dto);

        if(dto.getFiles()!=null && dto.getFiles().length > 0){
            ResponseEntity<?> result = service.upload(dto.getFiles(), "notice");

            @SuppressWarnings("unchecked")
            Map<String, Object> bodyMap = (Map<String, Object>) result.getBody();

            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, String>> uploadedFiles = objectMapper.convertValue(
                    bodyMap.get("fileData"),
                    new TypeReference<List<Map<String, String>>>() {}
            );

            for (int i = 0; i < dto.getFiles().length; i++) {
                MultipartFile file = dto.getFiles()[i];
                Map<String, String> uploaded = uploadedFiles.get(i);
                log.info("get..imageUrl=======>{}",uploaded.get("imageUrl"));
                NoticeFile noticeFile = NoticeFile.builder()
                        .originalName(file.getOriginalFilename())
                        .savedName(uploaded.get("imageUrl")
                                .substring(uploaded.get("imageUrl").lastIndexOf("/") + 1))
                        .filePath(uploaded.get("imageUrl"))
                        .thumbnailPath(uploaded.get("thumbnailUrl"))
                        .notice(notice)
                        .build();
                log.info("NoticeFile=======>{}",noticeFile);
                notice.addFile(noticeFile);
            }
        }

        repository.save(notice);
    }

    @Override
    public NoticeDTO getOne(Long noticeId){
        Notice notice = repository.findById(noticeId)
                .orElseThrow(() -> new RuntimeException("공지사항을 찾을 수 없습니다."));

        List<NoticeFileDTO> fileDTOList = notice.getFiles().stream()
                .map(file -> NoticeFileDTO.builder()
                        .id(file.getId())
                        .originalName(file.getOriginalName())
                        .savedName(file.getSavedName())
                        .filePath(file.getFilePath())
                        .thumbnailPath(file.getThumbnailPath())
                        .build())
                .toList();

        return NoticeDTO.builder()
                .noticeId(notice.getNoticeId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt())
                .viewCount(notice.getViewCount())
                .fileList(fileDTOList)
                .build();
    }
}