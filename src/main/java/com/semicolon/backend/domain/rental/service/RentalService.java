package com.semicolon.backend.domain.rental.service;

import com.semicolon.backend.domain.rental.dto.RentalDTO;
import com.semicolon.backend.domain.rental.entity.Rental;

import java.util.List;

public interface RentalService {
    public Rental register(String loginIdFromToken, RentalDTO rentalDTO);
    public List<RentalDTO> getList(String loginIdFromToken);
    public void deleteOne(String loginIdFromToken, Long rentalId);
}
