package com.semicolon.backend.global.pageable;

import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Data
public class PageRequestDTO {
    private int page = 1;
    private int size = 10;
    private String keyword;
    private String type;
    private String sort;
}
