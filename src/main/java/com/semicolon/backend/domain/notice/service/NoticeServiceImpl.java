package com.semicolon.backend.domain.notice.service;

import com.semicolon.backend.domain.notice.dto.NoticeDTO;
import com.semicolon.backend.domain.notice.dto.NoticeFileDTO;
import com.semicolon.backend.domain.notice.entity.Notice;
import com.semicolon.backend.domain.notice.entity.NoticeFile;
import com.semicolon.backend.domain.notice.repository.NoticeRepository;
import com.semicolon.backend.global.file.service.FileUploadService;
import com.semicolon.backend.global.pageable.PageRequestDTO;
import com.semicolon.backend.global.pageable.PageResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        Notice notice = repository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다."));
        for (NoticeFile file : notice.getFiles()){
            service.deleteFile(file.getFilePath(),file.getThumbnailPath());
        }
        repository.delete(notice);
    }

    @Override
    public PageResponseDTO<NoticeDTO> list(PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage()-1,pageRequestDTO.getSize()
        , Sort.by("createdAt").descending()
        );
        String keyword = pageRequestDTO.getKeyword();
        String type = pageRequestDTO.getType();
        Page<Notice> result;
        if(keyword==null || keyword.isEmpty()){
            result = repository.findAll(pageable);
        } else {
            if ("c".equals(type)) {
            result=repository.findByContentContaining(keyword, pageable);
        } else if ("t".equals(type)) {
            result=repository.findByTitleContaining(keyword, pageable);
        } else {
            result=repository.findByContentContainingOrTitleContaining(keyword,keyword,pageable);
        }
        }
        List<NoticeDTO> dtoList = result.getContent().stream().map(ent->toDto(ent)).collect(Collectors.toList());
        return PageResponseDTO.<NoticeDTO>withAll()
                .dtoList(dtoList)
                .pageRequestDTO(pageRequestDTO)
                .totalCnt(result.getTotalElements())
                .build();
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

        Notice newNotice = mapper.map(dto, Notice.class);
        newNotice.setViewCount(0);
        newNotice.setCreatedAt(LocalDateTime.now());

        if (dto.getFileList() != null && !dto.getFileList().isEmpty()) {
            List<NoticeFile> noticeFiles = dto.getFileList().stream()
                    .map(fileDto -> mapper.map(fileDto, NoticeFile.class))
                    .toList();
            noticeFiles.forEach(i->newNotice.addFile(i));
        }
        repository.save(newNotice);
    }

    @Override
    @Transactional
    public void modify(Long id, NoticeDTO dto) {

        Notice notice = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다."));

        notice.setTitle(dto.getTitle());
        notice.setContent(dto.getContent());

        if (dto.getRemoveFileId() != null && !dto.getRemoveFileId().isEmpty()) {

            List<NoticeFile> filesToDelete = notice.getFiles().stream()
                    .filter(f -> dto.getRemoveFileId().contains(f.getId()))
                    .toList();

            for (NoticeFile file : filesToDelete) {
                service.deleteFile(file.getFilePath(), file.getThumbnailPath());
                notice.removeFile(file);
            }
        }

        if (dto.getFileList() != null) {
            dto.getFileList().stream()
                    .filter(f -> f.getId() == null)
                    .forEach(f -> {
                        NoticeFile noticeFile = NoticeFile.builder()
                                .originalName(f.getOriginalName())
                                .savedName(f.getSavedName())
                                .filePath(f.getFilePath())
                                .thumbnailPath(f.getThumbnailPath())
                                .build();
                        notice.addFile(noticeFile);
                    });
        }
        repository.save(notice);
    }

    @Override
    public NoticeDTO getOne(Long noticeId){
        Notice notice = repository.findById(noticeId)
                .orElseThrow(() -> new RuntimeException("공지사항을 찾을 수 없습니다."));
       return entityToDTO(notice);
    }

    private NoticeDTO entityToDTO(Notice notice){
        List<NoticeFileDTO> fileDTOList = notice.getFiles().stream()
                .map(i->{
                    return NoticeFileDTO.builder()
                            .id(i.getId())
                            .filePath(i.getFilePath())
                            .thumbnailPath(i.getThumbnailPath())
                            .savedName(i.getSavedName())
                            .originalName(i.getOriginalName())
                            .build();
                }).toList();
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