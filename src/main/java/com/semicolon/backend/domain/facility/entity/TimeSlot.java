package com.semicolon.backend.domain.facility.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimeSlot {
    T1("6:00 ~ 7:00"),
    T2("7:00 ~ 8:00"),
    T3("8:00 ~ 9:00"),
    T4("9:00 ~ 10:00"),
    T5("10:00 ~ 11:00"),
    T6("11:00 ~ 12:00"),
    T7("12:00 ~ 13:00"),
    T8("13:00 ~ 14:00"),
    T9("14:00 ~ 15:00"),
    T10("15:00 ~ 16:00"),
    T11("16:00 ~ 17:00"),
    T12("17:00 ~ 18:00"),
    T13("18:00 ~ 19:00"),
    T14("19:00 ~ 20:00"),
    T15("20:00 ~ 21:00"),
    T16("21:00 ~ 22:00");

    private final String description;
}
