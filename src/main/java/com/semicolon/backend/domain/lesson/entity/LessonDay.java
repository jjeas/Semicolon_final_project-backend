package com.semicolon.backend.domain.lesson.entity;

import lombok.Getter;

@Getter
public enum LessonDay {
    MON("월요일"),
    TUE("화요일"),
    WED("수요일"),
    THU("목요일"),
    FRI("금요일"),
    SAT("토요일");

    private final String label;

    LessonDay(String label) {
        this.label = label;
    }

}
