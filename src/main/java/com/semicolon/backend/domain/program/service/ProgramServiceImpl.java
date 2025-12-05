package com.semicolon.backend.domain.program.service;

import com.semicolon.backend.domain.guide.dto.GuideUploadDTO;
import com.semicolon.backend.domain.guide.entity.GuideUpload;
import com.semicolon.backend.domain.program.dto.ProgramDTO;
import com.semicolon.backend.domain.program.dto.ProgramReqDTO;
import com.semicolon.backend.domain.program.dto.ProgramUploadDTO;
import com.semicolon.backend.domain.program.entity.Program;
import com.semicolon.backend.domain.program.entity.ProgramUpload;
import com.semicolon.backend.domain.program.repository.ProgramRepository;
import com.semicolon.backend.domain.program.repository.ProgramUploadRepository;
import com.semicolon.backend.global.file.uploadFile.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProgramServiceImpl implements ProgramService{


    private final ProgramRepository repository;
    private final ProgramUploadRepository  programUploadRepository;
    private final CustomFileUtil customFileUtil;
    private final ModelMapper mapper;

    @Override
    public ProgramDTO getOne(long pno) {
        Program program = repository.findById(pno)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 프로그램입니다."));

        if(program == null){
            return new ProgramDTO();
        }
        List<ProgramUploadDTO> forUploadFiles = program.getUploads().stream().map(i->
                ProgramUploadDTO.builder()
                        .fileNo(i.getFileNo())
                        .filePath(i.getFilePath())
                        .fileName(i.getFileName())
                        .savedName(i.getSavedName())
                        .build()
        ).toList();


        return ProgramDTO.builder()
                .pno(program.getPno())
                .programName(program.getProgramName())
                .content(program.getContent())
                .uploadFiles(forUploadFiles)
                .build();
    }

    @Override
    public void update(ProgramReqDTO programReqDTO) {
        Program program = repository.findById(programReqDTO.getPno())
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 프로그램입니다."));
        program.setContent(programReqDTO.getContent());

        repository.save(program);

        if(programReqDTO.getDeletedNo() != null){
            for(String i : programReqDTO.getDeletedNo()){
                ProgramUpload programUpload = programUploadRepository.findById(Long.valueOf(i)).orElseThrow();
                program.removeFile(programUpload);
                customFileUtil.deleteFile(programUpload.getFilePath());
            }
        }

        if (programReqDTO.getFiles() != null && programReqDTO.getFiles().length > 0) {

            List<String> uploadFiles = customFileUtil.saveFiles(programReqDTO.getFiles(), "program");
            for (var i = 0; i < uploadFiles.size(); i += 2) {
                String customOriginalName = uploadFiles.get(i);
                String customSavedName = uploadFiles.get(i + 1);

                ProgramUpload programUpload = new ProgramUpload();
                programUpload.setFileName(customOriginalName);
                programUpload.setFilePath(customFileUtil.getUploadPath() + "/program/" + customSavedName);
                programUpload.setSavedName(customSavedName);
                programUpload.setProgram(program);
                programUploadRepository.save(programUpload);

            }

        }
    }

    @Override
    public List<ProgramDTO> getList() {
        List<ProgramDTO> list = repository.findAll().stream().map(i->mapper.map(i,ProgramDTO.class)).toList();
        return list;
    }
}