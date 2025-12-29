package com.semicolon.backend.domain.lesson.dto;

import com.semicolon.backend.domain.lesson.entity.LessonStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LessonListResDTO {
    private Long lessonId;
    private String title;
    private String partnerName;
    private String category;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<String> days;
    private LocalTime startTime;
    private LocalTime endTime;
    private String level;
    private String facilityType;
    private LessonStatus status;
    private boolean isRegistered;
    private String description;
    private Long minPeople;
    private Long maxPeople;
    private Long currentPeople;
    private LocalDate regEndDate;
    private Long price;

    public void checkEndDate(){
        LocalDateTime now = LocalDateTime.now();
        LocalDate now2=LocalDate.now();
        if(now.isBefore(this.regEndDate.atTime(LocalTime.MAX))){ //아직 신청마감 시간 아니면 함수 종료
            return;
        }
        if(this.regEndDate.equals(now2)){
            this.status=LessonStatus.CLOSED;
        }
        if(this.currentPeople<minPeople){ //마감일이 지났는데 최소 인원 미달성시
            this.status=LessonStatus.CANCELED; //취소로 바꿈
        }else{
            this.status=LessonStatus.ACCEPTED; //아니면 자동으로 허용됨으로 바꿈
        }
    }
}
