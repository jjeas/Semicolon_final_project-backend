package com.semicolon.backend.global.reservationFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@RestController
@Slf4j
@RequestMapping("/api/availableTimes")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationFilter reservationFilter;

    @GetMapping("")
    public ResponseEntity<List<LocalTime>> getAvailableTimes(@RequestParam Long spaceId,
                                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        return ResponseEntity.ok(reservationFilter.getAvailableTimes(spaceId, date));
    }

}
