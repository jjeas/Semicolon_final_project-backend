package com.semicolon.backend.domain.guide.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tbl_guide")
public class Guide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guide_id", nullable = false)
    private long id;

    @Column(name = "guide_category", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private GuideCategory category;

    @Lob
    @Column(name = "guide_html", nullable = false, columnDefinition = "LONGTEXT")
    private String html;

    @Column(name="guide_updated_date", nullable = false)
    private LocalDateTime updatedDate;

    @Builder.Default
    @OneToMany(mappedBy = "guide", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GuideUpload> uploads = new ArrayList<>();
}
