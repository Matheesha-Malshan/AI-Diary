package org.AiDiary.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;


@Getter
@Setter
@Document(collection = "userContent")
public class ContentEntity {
    @Id
    private String id;
    private Integer userId;
    private LocalDate createDate;
    private String userPrompt;

}
