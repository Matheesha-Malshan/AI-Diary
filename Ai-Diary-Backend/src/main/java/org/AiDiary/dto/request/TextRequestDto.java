package org.AiDiary.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class TextRequestDto {
    private int user_id;
    private String promt;
    private Date createDate;
}
