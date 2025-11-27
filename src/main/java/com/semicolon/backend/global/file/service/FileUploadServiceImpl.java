package com.semicolon.backend.global.file.service;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
@Slf4j
@Service
public class FileUploadServiceImpl implements FileUploadService{

    @Value("${com.semicolon.backend.upload}")
    private String uploadDir;

    @Override
    public ResponseEntity<?> upload(MultipartFile[] files, String domain){
        if(files.length==0 || files==null) return ResponseEntity.badRequest().body("파일이 존재하지 않습니다.");
        //file 이 존재하지 않을 때 예외처리
        try {
            List<Map<String,String>> fileDataList =new ArrayList<>();
            String subDirPath = uploadDir+File.separator+domain; // 도메인으로 목적에 따라 다른 폴더에 저장
            File uploadDirectory = new File(subDirPath); //File 객체에 저장경로 생성자로 만들기
            if(!uploadDirectory.exists()) uploadDirectory.mkdirs(); // 파일경로가 존재하지 않으면 새로 만든다
            log.info("files={}",files[0].getOriginalFilename());
            for(MultipartFile file : files){
                if(file.isEmpty()) continue;//파일이 비어있으면 넘기기
                String originalName = file.getOriginalFilename(); // 업로드파일 원본 이름
                String extension = originalName.substring(originalName.lastIndexOf(".")); //파일 형식 ex) .jpg .xlm 등
                String uniqueName = UUID.randomUUID().toString(); //" 파일별 고유 이름 설정을 위한 UUID
                String originalFileName = uniqueName+extension; //원본파일 저장경로
                File originalFile = new File(subDirPath+File.separator+originalFileName);
                String thumbnailFileName ="s_"+uniqueName+extension;//썸네일파일 저장경로
                File thumbnailFile = new File(subDirPath+File.separator+thumbnailFileName);
                file.transferTo(originalFile); // 원본파일 저장
                if(isImageFile(originalFile.toPath())){
                    try(FileOutputStream thumbnailOS = new FileOutputStream(thumbnailFile)){
                        Thumbnails.of(originalFile)
                                .size(100,100)
                                .toOutputStream(thumbnailOS);
                    }
                }
                Map<String, String> urls=new HashMap<>();
                urls.put("imageUrl","/upload/"+domain+"/"+originalFileName);
                urls.put("thumbnailUrl","/upload/"+domain+"/"+thumbnailFileName);
                fileDataList.add(urls);
            }
            Map<String, Object> res = new HashMap<>(); //응답을 위한 Map 생성
            res.put("fileData", fileDataList); //웹에서 접근하는 URL 경로
            return ResponseEntity.ok(res);
        }catch (IOException e){
            e.printStackTrace();
            return ResponseEntity.status(500).body("파일 저장 중 오류 발생, "+e.getMessage());
        }
    }
    private boolean isImageFile(Path path) { //MIME 표준 규약으로 이미지인지 확인하는 함수
        try {
            String mimeType = Files.probeContentType(path);
            return mimeType != null && mimeType.startsWith("image");
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void deleteFile(String filePath, String thumbnailPath) {
        if (filePath != null && filePath.startsWith("/upload/")) {
            String relativePath = filePath.substring("/upload/".length());
            Path originalFilePath = Path.of(uploadDir, relativePath);
            try {
                // 원본 파일 삭제
                if (Files.exists(originalFilePath)) {
                    Files.delete(originalFilePath);
                    log.info("원본 파일 삭제 완료: {}", originalFilePath);
                }
            } catch (IOException e) {
                log.error("원본 파일 삭제 실패:{}",  e.getMessage());
            }
        }

        if (thumbnailPath != null && thumbnailPath.startsWith("/upload/")) {
            String relativePath = thumbnailPath.substring("/upload/".length());
            Path thumbnailFilePath = Path.of(uploadDir, relativePath);

            try {
                if (Files.exists(thumbnailFilePath)) {
                    Files.delete(thumbnailFilePath);
                    log.info("썸네일 파일 삭제 완료: {}", thumbnailFilePath);
                }
            } catch (IOException e) {
                log.error("썸네일 파일 삭제 실패: {}",  e.getMessage());
            }
        }
    }

}
