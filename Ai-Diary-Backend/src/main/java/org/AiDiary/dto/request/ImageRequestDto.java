package org.AiDiary.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
public class ImageRequestDto {

    private Integer user_id;
    private LocalDate createDate;
    private MultipartFile image;
    private String description;
    private int entryId;
}
