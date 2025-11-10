package com.semicolon.backend.global.file;

import com.semicolon.backend.global.file.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class FileController {

    private final FileUploadService service;

    @PostMapping("/{domain}")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile[] files, @PathVariable("domain") String domain){
        return service.upload(files,domain);
    }
}
