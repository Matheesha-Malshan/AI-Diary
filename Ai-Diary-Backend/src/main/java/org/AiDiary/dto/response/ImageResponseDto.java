package org.AiDiary.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ImageResponseDto {

    List<String> image=new ArrayList<>();
    List<LocalDate> dateList=new ArrayList<>();
    List<String> chunkList=new ArrayList<>();

}
