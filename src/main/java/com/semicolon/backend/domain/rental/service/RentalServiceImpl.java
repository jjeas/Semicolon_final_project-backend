package com.semicolon.backend.domain.rental.service;

import com.semicolon.backend.domain.facility.entity.FacilitySpace;
import com.semicolon.backend.domain.facility.repository.FacilitySpaceRepository;
import com.semicolon.backend.domain.lesson.entity.LessonStatus;
import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.member.repository.MemberRepository;
import com.semicolon.backend.domain.payment.entity.Payment;
import com.semicolon.backend.domain.payment.service.PaymentService;
import com.semicolon.backend.domain.rental.dto.RentalDTO;
import com.semicolon.backend.domain.rental.entity.Rental;
import com.semicolon.backend.domain.rental.entity.RentalStatus;
import com.semicolon.backend.domain.rental.repository.RentalRepository;
import com.semicolon.backend.global.pageable.PageRequestDTO;
import com.semicolon.backend.global.pageable.PageResponseDTO;
import com.semicolon.backend.global.reservationFilter.ReservationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentalServiceImpl implements RentalService {

    private final ReservationFilter reservationFilter;
    private final MemberRepository memberRepository;
    private final FacilitySpaceRepository facilitySpaceRepository;
    private final RentalRepository rentalRepository;
    private final ModelMapper mapper;
    private final PaymentService paymentService;

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
                .createdAt(LocalDateTime.now())
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
        Payment payment = rental.getPayment();
        paymentService.cancelPayment(payment.getPaymentId(), "회원 요청");
        rentalRepository.delete(rental);
    }

    @Override
    public PageResponseDTO<RentalDTO> getAllList(PageRequestDTO dto) {

        Pageable pageable = PageRequest.of(dto.getPage() - 1, dto.getSize());

        Page<Rental> result = rentalRepository.searchAll(
                dto.getStatus() == null || dto.getStatus().isEmpty() ? null : RentalStatus.valueOf(dto.getStatus().toUpperCase()),
                dto.getKeyword() == null || dto.getKeyword().trim().isEmpty() ? null : dto.getKeyword().trim(),
                dto.getStartDate() != null ? dto.getStartDate().atStartOfDay() : null,
                dto.getEndDate() != null ? dto.getEndDate().atTime(23, 59, 59) : null,
                pageable
        );

        List<RentalDTO> dtoList = result.getContent().stream()
                .map(r -> RentalDTO.builder()
                        .id(r.getId())
                        .spaceId(r.getSpace().getId())
                        .facilityName(r.getSpace().getFacility().getFacilityName())
                        .spaceName(r.getSpace().getSpaceName())
                        .startTime(r.getStartTime())
                        .endTime(r.getEndTime())
                        .price(r.getPrice())
                        .name(r.getName())
                        .phoneNumber(r.getPhoneNumber())
                        .memo(r.getMemo())
                        .status(r.getStatus().name())
                        .build())
                .toList();

        return PageResponseDTO.<RentalDTO>withAll()
                .dtoList(dtoList)
                .pageRequestDTO(dto)
                .totalCnt(result.getTotalElements())
                .build();
    }

    @Override
    public RentalDTO getOne(Long id) {
        Rental rental = rentalRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 대관 내역이 존재하지 않습니다."));
        RentalDTO dto = mapper.map(rental, RentalDTO.class);
        dto.setFacilityName(rental.getSpace().getFacility().getFacilityName());
        return dto;
    }

    @Override
    public void statusChange(Long id, RentalStatus status) {
        Rental rental = rentalRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 대관 내역이 존재하지 않습니다."));
        rental.setStatus(status);

        Payment payment = rental.getPayment();
        if(payment!=null){
            paymentService.cancelPayment(payment.getPaymentId(),"반려로 인한 결제 취소");
        }

        rentalRepository.save(rental);
    }

}
