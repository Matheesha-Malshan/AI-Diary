package org.AiDiary.dbQuery.impl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.AiDiary.dbQuery.DbStrategy;
import org.AiDiary.dto.response.IntentResponseDto;
import org.AiDiary.entity.ContentEntity;
import org.AiDiary.enums.MongoQueryIntent;
import org.AiDiary.enums.Query;
import org.AiDiary.repository.ContentRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


@RequiredArgsConstructor
@Component
public class MongoStrategy implements DbStrategy {

    private final ContentRepository contentRepository;

    @Override
    public boolean checkType(Query query) {
        return Query.MongoDb==query;
    }
    @Override
    public IntentResponseDto processQuery(JsonNode jsonNode, int userId, String userQuery){


        String selector=jsonNode.findValue("selector").asText();

        IntentResponseDto intentResponseDto=new IntentResponseDto();

        ZoneId zone = ZoneId.systemDefault();


        switch (MongoQueryIntent.valueOf(selector)){

            case Specific_date,Between_two_days-> {

                try{
                    Instant start= Instant.parse(jsonNode.findValue("start_Date").asText());
                    Instant end=Instant.parse(jsonNode.findValue("end_Date").asText());

                    List<ContentEntity>contentEntities=
                            contentRepository.findByUserIdAndCreateDateBetween(userId,start,end);

                    for (ContentEntity content:contentEntities){

                        LocalDate date = content.getCreateDate().atZone(zone).toLocalDate();
                        intentResponseDto.getDateList().add(date);
                        intentResponseDto.getContentList().add(content.getUserPrompt());
                    }
                    return intentResponseDto;

                } catch (RuntimeException e) {
                    throw new RuntimeException(e);
                }

            }

            case Last_dateLimit_date,Last_month,Last_week,Last_date -> {

                try{
                    int limit=jsonNode.findValue("limit").asInt();

                    Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createDate"));
                    List<ContentEntity> contentEntities = contentRepository.findByUserId(userId, pageable);

                    for (ContentEntity content:contentEntities){

                        LocalDate date = content.getCreateDate().atZone(zone).toLocalDate();
                        intentResponseDto.getDateList().add(date);
                        intentResponseDto.getContentList().add(content.getUserPrompt());
                    }
                    return intentResponseDto;

                } catch (RuntimeException e) {
                    throw new RuntimeException(e);
                }

            }
        }
        return intentResponseDto;
    }


}
