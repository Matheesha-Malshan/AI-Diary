package org.AiDiary.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EmbeddingSavingDto {
    private int userId;
    private List<float[]> embedding=new ArrayList<>();
    private List<String> chunk=new ArrayList<>();
}
