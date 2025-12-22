package org.AiDiary.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class VectorSearchDto {

    private Integer userId;
    private List<String> chunkList;
    private List<LocalDate> dateList;

}
