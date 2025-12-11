package com.semicolon.backend.domain.guide.service;

import com.semicolon.backend.domain.guide.dto.GuideDTO;
import com.semicolon.backend.domain.guide.dto.GuideReqDTO;
import com.semicolon.backend.domain.guide.dto.GuideUploadDTO;
import com.semicolon.backend.domain.guide.entity.Guide;
import com.semicolon.backend.domain.guide.entity.GuideCategory;
import com.semicolon.backend.domain.guide.entity.GuideUpload;
import com.semicolon.backend.domain.guide.repository.GuideRepository;
import com.semicolon.backend.domain.guide.repository.GuideUploadRepository;
import com.semicolon.backend.global.file.uploadFile.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GuideServiceImpl implements GuideService{

    private final GuideRepository guideRepository;
    private final GuideUploadRepository guideUploadRepository;
    private final CustomFileUtil customFileUtil;

    @Override
    public GuideDTO get(GuideCategory category) {
        Guide guide = guideRepository.findByCategory(category)
                .orElse(null);

        if (guide == null) {
            return GuideDTO.builder()
                    .category(category)
                    .html(null)
                    .updatedDate(LocalDateTime.now())
                    .uploadFiles(null)
                    .build();
        }

        List<GuideUploadDTO> forUploadFiles = guide.getUploads().stream().map(i->
                GuideUploadDTO.builder()
                        .fileNo(i.getFileNo())
                        .filePath(i.getFilePath())
                        .fileName(i.getFileName())
                        .savedName(i.getSavedName())
                        .build()
        ).toList();

        GuideDTO guideDTO = new GuideDTO();
        guideDTO.setId(guide.getId());
        guideDTO.setCategory(guide.getCategory());
        guideDTO.setHtml(guide.getHtml());
        guideDTO.setUpdatedDate(guide.getUpdatedDate());
        guideDTO.setUploadFiles(forUploadFiles);


        return guideDTO;
    }

    @Override
    public void upload(GuideReqDTO guideReqDTO) {
            Guide guide = guideRepository.findByCategory(guideReqDTO.getCategory()).orElseThrow(() -> new IllegalArgumentException("해당 카테고리의 Guide 데이터가 존재하지 않습니다."));;
            guide.setHtml(guideReqDTO.getHtml());
            guide.setCategory(guideReqDTO.getCategory());
            guide.setUpdatedDate(LocalDateTime.now());

            guideRepository.save(guide);

            if(guideReqDTO.getDeletedNo() != null){
                for(String i : guideReqDTO.getDeletedNo()) {
                    GuideUpload guideUpload = guideUploadRepository.findById(Long.valueOf(i)).orElseThrow();
                    guideUploadRepository.deleteById(Long.valueOf(i));
                    customFileUtil.deleteFile(guideUpload.getFilePath());
                }

            }

            if (guideReqDTO.getFiles() != null && guideReqDTO.getFiles().length > 0) {

            List<String> uploadFiles = customFileUtil.saveFiles(guideReqDTO.getFiles(), "guide");
            for(var i=0; i<uploadFiles.size(); i+=2){
                String customOriginalName = uploadFiles.get(i);
                String customSavedName = uploadFiles.get(i+1);

                GuideUpload guideUpload = new GuideUpload();
                guideUpload.setFileName(customOriginalName);
                guideUpload.setFilePath(customFileUtil.getUploadPath() + "/guideFiles/" + customSavedName);
                guideUpload.setSavedName(customSavedName);
                guideUpload.setGuide(guide);

                guideUploadRepository.save(guideUpload);
            }
        }

    }

}
