package org.AiDiary.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
public class VoiceRequestDto {
    private Integer userId;
    private LocalDate createDate;
    private double duration;
    private double fileSize;
    private String format;
    private MultipartFile audio;
}
