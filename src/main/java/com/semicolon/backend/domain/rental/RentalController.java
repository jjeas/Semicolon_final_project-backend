package com.semicolon.backend.domain.rental;

import com.semicolon.backend.domain.dailyUse.dto.DailyUseDTO;
import com.semicolon.backend.domain.rental.dto.RentalDTO;
import com.semicolon.backend.domain.rental.service.RentalService;
import com.semicolon.backend.global.reservationFilter.ReservationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/reservation/rental")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService service;

    @PostMapping("")
    public ResponseEntity<String> rentalRegister (@AuthenticationPrincipal String loginIdFromToken, @RequestBody RentalDTO rentalDTO){
        service.register(loginIdFromToken, rentalDTO);
        return ResponseEntity.ok("예약 완료");
    }

}
