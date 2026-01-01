package org.AiDiary.dbQuery.impl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.AiDiary.dbQuery.DbStrategy;
import org.AiDiary.dto.response.IntentResponseDto;
import org.AiDiary.entity.UserChatEntity;
import org.AiDiary.enums.Query;
import org.AiDiary.enums.SqlQueryIntent;
import org.AiDiary.exception.UserNotFoundException;
import org.AiDiary.repository.UserChatRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;



@RequiredArgsConstructor
@Component
public class SqlStrategyImpl implements DbStrategy {

    private final UserChatRepository userChatRepository;

    @Override
    public boolean checkType(Query query) {
        return query==Query.Sql;
    }

    @Override
    public IntentResponseDto processQuery(JsonNode jsonNode, int userId, String userQuery){

        String selector=jsonNode.findValue("selector").asText();

        IntentResponseDto intentResponseDto=new IntentResponseDto();

        switch (SqlQueryIntent.valueOf(selector)){

            case Last_happiest_day -> {

                UserChatEntity chat=userChatRepository.
                        findFirstByUserEntity_UserIdOrderBySentimentScoreDescCreateDateDesc(userId)
                        .orElseThrow(() -> new UserNotFoundException("user record not found"));

                intentResponseDto.getDateList().add(chat.getCreateDate());
                return intentResponseDto;

            }
            case very_happy_days -> {
                List<UserChatEntity> chat=userChatRepository.
                        findAllByUserEntity_UserIdOrderBySentimentScoreDescCreateDateDesc(userId);

                try{
                    for (UserChatEntity u:chat){
                        intentResponseDto.getDateList().add(u.getCreateDate());
                    }
                    return intentResponseDto;

                } catch (RuntimeException e) {
                    throw new RuntimeException();
                }

            }

            case Last_saddest_day -> {

                UserChatEntity chat=userChatRepository.
                        findFirstByUserEntity_UserIdOrderBySentimentScoreAscCreateDateDesc(userId).
                        orElseThrow(()->new UserNotFoundException("user record not found"));

                intentResponseDto.getDateList().add(chat.getCreateDate());
                return intentResponseDto;

            }

            case Happiest_week -> {
                Map<String, Object> chat= userChatRepository.findHappiestWeek(userId);

                try {
                    intentResponseDto.setMessage(chat.values().toString());
                    return intentResponseDto;

                } catch (RuntimeException e) {

                    throw new UserNotFoundException("not found record");
                }

            }

            case Happiest_month -> {

                Map<String, Object> chat=userChatRepository.findHappiestMonth(userId);
                try {
                    intentResponseDto.setMessage(chat.values().toString());
                    return intentResponseDto;

                } catch (RuntimeException e) {

                    throw new UserNotFoundException("not found record");
                }

            }
            case Most_emotional_day -> {
                Map<String, Object> chat=userChatRepository.findMostEmotionalDay(userId);
                try {
                    intentResponseDto.setMessage(chat.values().toString());
                    return intentResponseDto;

                } catch (RuntimeException e) {

                    throw new UserNotFoundException("not found record");
                }

            }

        }
       return intentResponseDto;

    }
}
