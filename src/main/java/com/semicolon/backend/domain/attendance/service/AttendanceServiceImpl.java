package com.semicolon.backend.domain.attendance.service;

import com.semicolon.backend.domain.attendance.dto.AttendanceDTO;
import com.semicolon.backend.domain.attendance.entity.Attendance;
import com.semicolon.backend.domain.attendance.entity.AttendanceStatus;
import com.semicolon.backend.domain.attendance.repository.AttendanceRepository;
import com.semicolon.backend.domain.lesson.entity.Lesson;
import com.semicolon.backend.domain.lesson.repository.LessonRepository;
import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.member.repository.MemberRepository;
import com.semicolon.backend.domain.registration.dto.RegistrationDTO;
import com.semicolon.backend.domain.registration.entity.Registration;
import com.semicolon.backend.domain.registration.repository.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {
    private final MemberRepository memberRepository;
    private final RegistrationRepository registrationRepository;
    private final LessonRepository lessonRepository;
    private final AttendanceRepository attendanceRepository;


    @Override
    public List<AttendanceDTO> getList(String loginIdFromToken, Long lessonNo, LocalDate date) {
        Lesson findLesson = lessonRepository.findLessonByPartnerAndId(loginIdFromToken, lessonNo);
        // 내가 맡은 레슨인지 체크
        if (findLesson == null) {
            throw new IllegalArgumentException("내 레슨이 아닙니다");
        }

        List<Registration> registrations = registrationRepository.findAllByLessonId(lessonNo);
        // 특정 레슨 번호(lessonId)로 해당 강좌를 신청한 사람들 목록을 가져옴

        return registrations.stream().map(i -> {
            Optional<Attendance> attendanceCheck = attendanceRepository.findAttendance(
                    lessonNo,
                    i.getMember().getMemberId(),
                    date
            ); // 학생마다 해당 날짜 출석 기록이 있는지 확인

            return AttendanceDTO.builder()
                    .lessonId(lessonNo)
                    .studentNo(i.getMember().getMemberId())
                    .attendanceId(attendanceCheck.map(Attendance::getAttendanceId).orElse(null))
                    .name(i.getMember().getMemberName())
                    .attendanceDate(attendanceCheck.map(Attendance::getAttendanceDate).orElse(null))
                    .status(attendanceCheck.map(Attendance::getStatus).orElse(AttendanceStatus.NONE))
                    .memo(attendanceCheck.map(Attendance::getMemo).orElse(""))
                    .build();

        }).toList();
    }

    @Override
    public void save(String loginIdFromToken, List<AttendanceDTO> requestList, Long lessonNo) {
        Lesson findLesson = lessonRepository.findLessonByPartnerAndId(loginIdFromToken, lessonNo);
        // 내가 맡은 레슨인지 체크
        if (findLesson == null) {
            throw new IllegalArgumentException("내 레슨이 아닙니다");
        }

        requestList.forEach(i -> {

            Optional<Attendance> attendanceCheck = attendanceRepository.findAttendance(
                    lessonNo,
                    i.getStudentNo(),
                    i.getAttendanceDate()
            ); // 학생마다 해당 날짜 출석 기록이 있는지 확인

            Attendance attendance = attendanceCheck.orElseGet(()->
                    Attendance.builder()
                        .lesson(findLesson)
                        .status(AttendanceStatus.NONE)
                        .memo("")
                        .member(memberRepository.findById(i.getStudentNo())
                                .orElseThrow(() -> new IllegalArgumentException("회원이 없습니다.")))
                        .attendanceDate(i.getAttendanceDate())
                        .build()
            );

            attendance.setStatus(i.getStatus());
            attendance.setMemo(i.getMemo());

            attendanceRepository.save(attendance);
        });
    }
}
