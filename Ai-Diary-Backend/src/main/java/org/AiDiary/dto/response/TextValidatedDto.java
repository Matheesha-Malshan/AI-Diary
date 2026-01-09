package org.AiDiary.dto.response;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class TextValidatedDto {

    private String userQuery;
    private String proccesedQuery;
    private String errorType;
    private boolean presentError;
}
