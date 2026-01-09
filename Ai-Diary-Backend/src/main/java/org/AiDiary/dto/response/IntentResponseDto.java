package org.AiDiary.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class IntentResponseDto {

    private List<LocalDate> dateList=new ArrayList<>();
    private List<String> contentList=new ArrayList<>();
    private List<Double> sentimentList=getSentimentList();
    private boolean isPresentError;
    private String error;
    private String message;

}
