package com.semicolon.backend.global.file.uploadFile;

import jakarta.annotation.PostConstruct;
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

    public String getUploadPath() {
        return uploadPath;
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

}

