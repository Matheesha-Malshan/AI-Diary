package org.AiDiary.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TextRequestDto {
    private int userId;
    private String promt;
}
