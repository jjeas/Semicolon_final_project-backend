package com.semicolon.backend.domain.lesson.entity;

import jakarta.persistence.*;
import lombok.*;

import javax.sound.midi.Patch;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Table(name = "tbl_lesson_schedule")
@ToString
public class LessonSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lessonSchedule_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @ElementCollection(targetClass = LessonDay.class)
    @CollectionTable(name = "lesson_schedule_days", joinColumns = @JoinColumn(name = "lesson_schedule_id"))
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private List<LessonDay> lessonDay = new ArrayList<>();

    @Column(name = "lessonSchedule_startTime")
    private LocalTime startTime;
    @Column(name = "lessonSchedule_endTime")
    private LocalTime endTime;
}
