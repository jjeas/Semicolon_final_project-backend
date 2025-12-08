package com.semicolon.backend.domain.attendance.service;

import com.semicolon.backend.domain.attendance.dto.AttendanceDTO;
import com.semicolon.backend.domain.attendance.entity.Attendance;
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

        Member member = memberRepository.findByMemberLoginId(loginIdFromToken).orElseThrow(() -> new IllegalArgumentException("조회되는 멤버가 없습니다"));
        Lesson lesson = lessonRepository.findLessonByPartnerAndId(loginIdFromToken, lessonNo);
        if (lesson == null) throw new IllegalArgumentException("내 레슨이 아닙니다");

        List<Registration> registrations = registrationRepository.findAllByLessonId(member.getMemberId(), lessonNo);
        log.info("이건진심와야함=>{}", registrations);

        return registrations.stream().map(i -> {
            Optional<Attendance> oneAttendance = attendanceRepository.findAttendance(
                    lessonNo,
                    i.getMember().getMemberId(),
                    date
            );

            return AttendanceDTO.builder()
                    .lessonId(lessonNo)
                    .studentNo(i.getMember().getMemberId())
                    .attendanceId(oneAttendance.map(Attendance::getAttendanceId).orElse(null))
                    .name(i.getMember().getMemberName())
                    .attendanceDate(oneAttendance.map(Attendance::getAttendanceDate).orElse(null))
                    .status(oneAttendance.map(Attendance::getStatus).orElse(null))
                    .memo(oneAttendance.map(Attendance::getMemo).orElse(""))
                    .build();

        }).toList();
    }

    @Override
    public void save(String loginIdFromToken, List<AttendanceDTO> requestList, Long lessonNo) {
        Lesson findLesson = lessonRepository.findLessonByPartnerAndId(loginIdFromToken, lessonNo);

        requestList.forEach(i -> {

            Optional<Attendance> attendanceCheck = attendanceRepository.findAttendance(
                    lessonNo,
                    i.getStudentNo(),
                    i.getAttendanceDate()
            );

            Attendance attendance;

            if(attendanceCheck.isPresent()) {
                attendance = attendanceCheck.get();
            } else {
                attendance = Attendance.builder()
                        .lesson(findLesson)
                        .member(memberRepository.findById(i.getStudentNo()).orElseThrow(() -> new IllegalArgumentException("회원이 없습니다.")))
                        .attendanceDate(i.getAttendanceDate())
                        .build();
            }

            attendance.setStatus(i.getStatus());
            attendance.setMemo(i.getMemo());

            attendanceRepository.save(attendance);
        });
    }
}
