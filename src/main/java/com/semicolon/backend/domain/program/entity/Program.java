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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_program")
    @SequenceGenerator(name = "seq_program",sequenceName = "SEQ_PROGRAM", allocationSize = 1)
    private long pno;

    @Column(name = "content", nullable = false, columnDefinition = "CLOB")
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
