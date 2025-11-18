package com.semicolon.backend.global.file.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    public ResponseEntity<?> upload(MultipartFile[] files
            , String domain);
    void deleteFile(String filePath, String thumbnailPath);
}
