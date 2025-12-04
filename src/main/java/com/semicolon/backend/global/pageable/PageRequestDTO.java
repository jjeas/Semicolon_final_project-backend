package com.semicolon.backend.global.pageable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageRequestDTO {
    private int page = 1;
    private int size = 10;
    private String keyword;
    private String type;
    private String role;

}
