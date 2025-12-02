package com.semicolon.backend.domain.rental.service;

import com.semicolon.backend.domain.rental.dto.RentalDTO;
import com.semicolon.backend.domain.rental.entity.Rental;

public interface RentalService {
    public Rental register(String loginIdFromToken, RentalDTO rentalDTO);
}
