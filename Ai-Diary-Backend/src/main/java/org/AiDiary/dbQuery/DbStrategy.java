package org.AiDiary.dbQuery;

import com.fasterxml.jackson.databind.JsonNode;
import org.AiDiary.dto.response.IntentResponseDto;
import org.AiDiary.enums.Query;

public interface DbStrategy {

    boolean checkType(Query query);
    IntentResponseDto processQuery(JsonNode jsonNode, int userId, String userQuery);
}
