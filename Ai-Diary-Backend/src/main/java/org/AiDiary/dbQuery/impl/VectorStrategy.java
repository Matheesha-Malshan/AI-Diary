package org.AiDiary.dbQuery.impl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.AiDiary.dbQuery.DbStrategy;
import org.AiDiary.dto.response.IntentResponseDto;
import org.AiDiary.dto.response.VectorSearchDto;
import org.AiDiary.embedding.EmbeddingCreateService;
import org.AiDiary.embedding.EmbeddingService;
import org.AiDiary.enums.Query;
import org.AiDiary.exception.UserNotFoundException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class VectorStrategy implements DbStrategy {

    private final EmbeddingCreateService embeddingCreateService;
    private final EmbeddingService embeddingService;

    @Override
    public boolean checkType(Query query) {
        return query==Query.VectorDb;
    }
    @Override
    public IntentResponseDto processQuery(JsonNode jsonNode, int userId, String userQuery) {

        try {
            float[] arr=embeddingCreateService.createEmbeddings(userQuery);
            VectorSearchDto searched=embeddingService.searchEmbeddings(arr,userId,0.55f,"Ai-diary");

            IntentResponseDto intentResponseDto=new IntentResponseDto();

            intentResponseDto.setDateList(searched.getDateList());
            intentResponseDto.setContentList(searched.getChunkList());

            return intentResponseDto;

        } catch (RuntimeException e) {
            throw new UserNotFoundException("user not found exception");
        }
    }

}
