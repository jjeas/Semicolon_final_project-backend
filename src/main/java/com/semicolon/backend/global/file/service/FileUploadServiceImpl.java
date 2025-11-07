package com.semicolon.backend.global.file.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class FileUploadServiceImpl implements FileUploadService{

    private String uploadDir="C:/Users/leekh/OneDrive/Documents/GitHub/final/backend/Semicolon_final_project-backend/upload";

    @Override
    public ResponseEntity<?> upload(MultipartFile[] files){
        if(files.length==0 || files==null) return ResponseEntity.badRequest().body("파일이 존재하지 않습니다.");
        try {
            List<String> filePaths =new ArrayList<>();
            File uploadDirectory = new File(uploadDir); //파일 저장 경로 설정
            if(uploadDirectory.exists()==false) uploadDirectory.mkdirs(); // 파일경로가 존재하지 않으면 새로 만든다
            for(MultipartFile file : files){
                if(file.isEmpty()) continue;
                String originalName = file.getOriginalFilename(); // 업로드파일 이름
                String extension = originalName.substring(originalName.lastIndexOf(".")); //파일 형식 ex) .jpg .xlm 등
                String uniqueName = UUID.randomUUID().toString()+extension; //" 파일별 고유 이름 설정을 위한 UUID
                File fileUploadPath=new File(uploadDir+File.separator+uniqueName);
                file.transferTo(fileUploadPath); // 저장파일을 복사하여 담는다
                filePaths.add("/upload"+uniqueName);
            }
            Map<String, Object> res = new HashMap<>(); //응답을 위한 Map 생성
            res.put("filePath", filePaths); //웹에서 접근하는 URL 경로
            return ResponseEntity.ok(res);
        }catch (IOException e){
            e.printStackTrace();
            return ResponseEntity.status(500).body("파일 저장 중 오류 발생, "+e.getMessage());
    }
}

}
