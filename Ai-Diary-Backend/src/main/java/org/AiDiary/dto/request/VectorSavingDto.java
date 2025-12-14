package org.AiDiary.dto.request;

import java.util.List;

public class VectorSavingDto {
    private int userId;
    private String userQuery;
    private List<float[]> vector;
}
