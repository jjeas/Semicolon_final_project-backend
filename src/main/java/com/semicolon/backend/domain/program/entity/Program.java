package com.semicolon.backend.domain.program.entity;

import jakarta.persistence.*;
import lombok.*;

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
}
