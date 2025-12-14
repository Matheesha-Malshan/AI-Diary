package org.AiDiary.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class UserRequestDto {

    private String name;
    private String email;
    private Date createdDate;

}
