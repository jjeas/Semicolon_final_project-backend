package com.semicolon.backend.domain.rental.service;

import com.semicolon.backend.domain.facility.entity.FacilitySpace;
import com.semicolon.backend.domain.facility.repository.FacilitySpaceRepository;
import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.member.repository.MemberRepository;
import com.semicolon.backend.domain.rental.dto.RentalDTO;
import com.semicolon.backend.domain.rental.entity.Rental;
import com.semicolon.backend.domain.rental.entity.RentalStatus;
import com.semicolon.backend.domain.rental.repository.RentalRepository;
import com.semicolon.backend.global.reservationFilter.ReservationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentalServiceImpl implements RentalService {

    private final ReservationFilter reservationFilter;
    private final MemberRepository memberRepository;
    private final FacilitySpaceRepository facilitySpaceRepository;
    private final RentalRepository rentalRepository;

    @Override
    public Rental register(String loginIdFromToken, RentalDTO rentalDTO) {
        Member member = memberRepository.findByMemberLoginId(loginIdFromToken).orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다"));
        FacilitySpace facilitySpace = facilitySpaceRepository.findById(rentalDTO.getSpaceId()).orElseThrow(() -> new IllegalArgumentException("시설 정보가 없습니다."));

        boolean check = reservationFilter.isAvailable(rentalDTO.getSpaceId(), rentalDTO.getStartTime(), rentalDTO.getEndTime());
        if (!check) {
            throw new IllegalArgumentException("예약이 불가능한 시간입니다.");
        }

        Rental rental = Rental.builder()
                .space(facilitySpace)
                .member(member)
                .status(RentalStatus.PENDING)
                .startTime(rentalDTO.getStartTime())
                .endTime(rentalDTO.getEndTime())
                .name(rentalDTO.getName())
                .phoneNumber(rentalDTO.getPhoneNumber())
                .memo(rentalDTO.getMemo())
                .price(rentalDTO.getPrice())
                .build();

        return rentalRepository.save(rental);

    }

    @Override
    public List<RentalDTO> getList(String loginIdFromToken) {
        Member member = memberRepository.findByMemberLoginId(loginIdFromToken).orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));
        List<Rental> rentals = rentalRepository.findByMember(member).stream().toList();
        return rentals.stream().map((i) -> RentalDTO.builder()
                .id(i.getId())
                .spaceId(i.getSpace().getId())
                .startTime(i.getStartTime())
                .endTime(i.getEndTime())
                .name(i.getName())
                .price(i.getPrice())
                .spaceName(i.getSpace().getSpaceName())
                .phoneNumber(i.getPhoneNumber())
                .memo(i.getMemo())

                .status(i.getStatus().name())
                .build()).toList();

    }

    @Override
    public void deleteOne(String loginIdFromToken, Long rentalId) {
        Member member = memberRepository.findByMemberLoginId(loginIdFromToken).orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));
        List<Rental> rentals = rentalRepository.findByMember(member).stream().toList();
        Rental rental = rentals.stream()
                .filter(i -> i.getId().equals(rentalId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 예약 정보가 없습니다."));
        rentalRepository.delete(rental);
    }
}
