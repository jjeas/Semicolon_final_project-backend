package com.semicolon.backend.domain.rental;

import com.semicolon.backend.domain.dailyUse.dto.DailyUseDTO;
import com.semicolon.backend.domain.rental.dto.RentalDTO;
import com.semicolon.backend.domain.rental.entity.RentalStatus;
import com.semicolon.backend.domain.rental.service.RentalService;
import com.semicolon.backend.global.pageable.PageRequestDTO;
import com.semicolon.backend.global.pageable.PageResponseDTO;
import com.semicolon.backend.global.reservationFilter.ReservationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("")
    public ResponseEntity<List<RentalDTO>> rentalList (@AuthenticationPrincipal String loginIdFromToken) {
        List<RentalDTO> DTOList = service.getList(loginIdFromToken);
        return ResponseEntity.ok(DTOList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRental (@AuthenticationPrincipal String loginIdFromToken, @PathVariable("id") Long rentalId){
        log.info("몇번이 들어옴? {}", rentalId);
        service.deleteOne(loginIdFromToken, rentalId);
        return ResponseEntity.ok("삭제 완료");
    }

    @GetMapping("/admin")
    public ResponseEntity<PageResponseDTO<RentalDTO>> getAllList(PageRequestDTO pageRequestDTO){
        return ResponseEntity.ok(service.getAllList(pageRequestDTO));
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<RentalDTO> getOne(@PathVariable("id") Long id){
        return ResponseEntity.ok(service.getOne(id));
    }
    @PostMapping("/admin/{id}")
    public ResponseEntity<String> changeStatus(@PathVariable("id") Long id, @RequestBody RentalStatus status){
        service.statusChange(id,status);
        return ResponseEntity.ok("상태 수정 성공");
    }


}
