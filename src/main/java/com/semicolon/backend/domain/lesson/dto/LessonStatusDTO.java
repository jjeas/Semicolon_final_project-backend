package com.semicolon.backend.domain.lesson.dto;

import com.semicolon.backend.domain.lesson.entity.LessonStatus;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LessonStatusDTO {
    private long id;
    private long price;
    private LessonStatus status;

}
