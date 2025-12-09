package com.semicolon.backend.domain.rental.service;

import com.semicolon.backend.domain.rental.dto.RentalDTO;
import com.semicolon.backend.domain.rental.entity.Rental;
import com.semicolon.backend.domain.rental.entity.RentalStatus;
import com.semicolon.backend.global.pageable.PageRequestDTO;
import com.semicolon.backend.global.pageable.PageResponseDTO;

import java.util.List;

public interface RentalService {
    public Rental register(String loginIdFromToken, RentalDTO rentalDTO);
    public List<RentalDTO> getList(String loginIdFromToken);
    public void deleteOne(String loginIdFromToken, Long rentalId);
    public PageResponseDTO<RentalDTO> getAllList(PageRequestDTO dto);
    public RentalDTO getOne(Long id);
    public void statusChange(Long id, RentalStatus status);
}
