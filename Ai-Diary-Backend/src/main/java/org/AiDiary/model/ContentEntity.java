package org.AiDiary.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Document(collation = "userContent")
public class ContentEntity {
    @Id
    private int userId;
    private Date createDate;
    private String userPrompt;
    private List<String> places;
    private List<String> people;
    private List<String> keyWord;


}
