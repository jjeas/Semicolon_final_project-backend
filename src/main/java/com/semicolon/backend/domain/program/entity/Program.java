package com.semicolon.backend.domain.program.entity;

import com.semicolon.backend.domain.guide.entity.GuideUpload;
import com.semicolon.backend.domain.notice.entity.NoticeFile;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_program")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Program {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pno;

    @Column(name = "content", nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "program_name",nullable = false)
    private String programName;

    @Builder.Default
    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramUpload> uploads = new ArrayList<>();

    public void removeFile(ProgramUpload file) {
        this.uploads.remove(file);
        file.setProgram(null);
    }
}
