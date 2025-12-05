package com.semicolon.backend.domain.program.entity;

import com.semicolon.backend.domain.guide.entity.Guide;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString(exclude = "program")
@Table(name="tbl_program_file")
public class ProgramUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long fileNo;

    private String fileName;
    private String filePath;
    private String savedName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pno")
    private Program program;
}
