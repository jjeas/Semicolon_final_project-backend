package com.semicolon.backend.global.schelduler;

import com.semicolon.backend.domain.lesson.entity.Lesson;
import com.semicolon.backend.domain.lesson.entity.LessonStatus;
import com.semicolon.backend.domain.lesson.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class LessonScheduler {
    private final LessonRepository lessonRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void autoCloseLesson(){ //마감일에 자동으로 status 마감으로 변경하는 함수
        LocalDate limitDate = LocalDate.now().plusDays(3);
        List<Lesson> expired = lessonRepository.findByLessonStatusAndStartDateBefore(
                LessonStatus.ACCEPTED,
                limitDate
        );
        for(Lesson lesson : expired){
            lesson.setLessonStatus(LessonStatus.CLOSED);
        }
    }
}
