package com.semicolon.backend.domain.lesson.entity;

import lombok.Getter;

import java.time.DayOfWeek;

@Getter
public enum LessonDay {
    MON("월요일"),
    TUE("화요일"),
    WED("수요일"),
    THU("목요일"),
    FRI("금요일"),
    SAT("토요일"),
    SUN("일요일");

    private final String label;

    LessonDay(String label) {
        this.label = label;
    }

    public static LessonDay fromDayOfWeek(DayOfWeek dayOfWeek) {
        switch(dayOfWeek) {
            case MONDAY: return MON;
            case TUESDAY: return TUE;
            case WEDNESDAY: return WED;
            case THURSDAY: return THU;
            case FRIDAY: return FRI;
            case SATURDAY: return SAT;
            case SUNDAY: return SUN;
            default: throw new IllegalArgumentException("Unsupported day: " + dayOfWeek);
        }
    }

}
