package org.AiDiary.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EmbeddingSavingDto {

    private int userId;
    private float[] embedding=new float[768];
    private String chunk;
    private LocalDate createDate;
    private Integer entryId;
}
