package org.AiDiary.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
public class UserRequestDto {

    private Integer userId;
    private String name;
    private String email;
    private LocalDate createdDate;

}
