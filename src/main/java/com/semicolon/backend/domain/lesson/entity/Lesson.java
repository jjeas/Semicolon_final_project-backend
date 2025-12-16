package com.semicolon.backend.domain.lesson.entity;

import com.semicolon.backend.domain.facility.entity.FacilitySpace;
import com.semicolon.backend.domain.member.entity.Member;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Table(name = "tbl_lesson")
@ToString(exclude = "schedules")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id")
    private long id;

    @Column(name = "lesson_title")
    private String title;
    @Column(name = "lesson_startDate")
    private LocalDate startDate;
    @Column(name = "lesson_endDate")
    private LocalDate endDate;
    @Column(name = "lesson_level")
    private String level;

    @Column(name = "lesson_description")
    private String description;
    @Column(name = "lesson_tools")
    private String tools;
    @Column(name = "lesson_memo")
    private String memo;
    @Column(name = "lesson_curriculum")
    private String curriculum;

    @Column(name = "lesson_minPeople")
    private long minPeople;
    @Column(name = "lesson_maxPeople")
    private long maxPeople;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id")
    private Member partnerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "lesson_status")
    private LessonStatus lessonStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FACILITY_SPACE_ID")
    private FacilitySpace facilitySpace;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    @Builder.Default
    private List<LessonSchedule> schedules = new ArrayList<>();

    @Column(name = "lesson_current_people")
    private long currentPeople;

    @Column(name = "lesson_price")
    private Long price;

    public void toList (LessonSchedule lessonSchedule){
        schedules.add(lessonSchedule);
        lessonSchedule.setLesson(this);
    }
}
