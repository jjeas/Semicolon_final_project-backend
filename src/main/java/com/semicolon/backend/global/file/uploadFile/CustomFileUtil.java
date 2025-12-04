package com.semicolon.backend.global.file.uploadFile;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.LongStream;

@Getter
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomFileUtil {

    @Value("${com.semicolon.backend.upload}")
    private String uploadPath;

    @PostConstruct
    public void init() {
        File tempFolder = new File(uploadPath);
        if (tempFolder.exists() == false) tempFolder.mkdir();
        uploadPath = tempFolder.getAbsolutePath();
        log.info("실제 업로드 경로 => {}", uploadPath);
    }

    public List<String> saveFiles(MultipartFile[] files, String category) throws RuntimeException {
        if (files == null || files.length == 0) return null;
        Path categoryPath  = Paths.get(uploadPath, category);
        File categoryFolder = categoryPath.toFile();
        if(!categoryFolder.exists()) categoryFolder.mkdirs();

        List<String> uploadNames = new ArrayList<>();
        for (MultipartFile i : files) {
            String originalName = i.getOriginalFilename();
            String savedName = UUID.randomUUID().toString() + "_" + originalName;
            Path savePath = Paths.get(categoryPath.toString(), savedName);
            try {
                Files.copy(i.getInputStream(), savePath);

                String contentType = i.getContentType();
                if (contentType != null && contentType.startsWith("image")) {
                    Path thumbnailPath = Paths.get(categoryPath.toString(), "s_" + savedName.trim());
                    Thumbnails.of(savePath.toFile()).size(100, 100).toFile(thumbnailPath.toFile());
                }
                uploadNames.add(originalName);
                uploadNames.add(savedName);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        log.info("uploadNames => {}", uploadNames);
        return uploadNames;
    }

    public void deleteFile(String filePath) {
        if (filePath == null) return;

        Path originalFilePath = Path.of(filePath);
        try {
            if (Files.exists(originalFilePath)) {
                Files.delete(originalFilePath);
                log.info("원본 파일 삭제 완료: {}", originalFilePath);
            }
        } catch (IOException e) {
            log.error("원본 파일 삭제 실패: {}", e.getMessage());
        }

        // 썸네일 처리
        Path thumbnailFilePath = originalFilePath.getParent().resolve("s_" + originalFilePath.getFileName());
        try {
            if (Files.exists(thumbnailFilePath)) {
                Files.delete(thumbnailFilePath);
                log.info("썸네일 파일 삭제 완료: {}", thumbnailFilePath);
            }
        } catch (IOException e) {
            log.error("썸네일 파일 삭제 실패: {}", e.getMessage());
        }
    }


}

