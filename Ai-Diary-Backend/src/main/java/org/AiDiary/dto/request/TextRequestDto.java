package org.AiDiary.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDate;

@Getter
@Setter
public class TextRequestDto {
    private Integer user_id;
    private String promt;
    private LocalDate createDate;
}
