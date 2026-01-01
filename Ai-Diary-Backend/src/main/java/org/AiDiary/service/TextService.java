package org.AiDiary.service;

import org.AiDiary.dto.request.TextRequestDto;
import org.AiDiary.dto.response.IntentResponseDto;
import org.AiDiary.dto.response.TextResponseDto;
import reactor.core.publisher.Mono;

public interface TextService {
    TextResponseDto createText(TextRequestDto textRequestDto);
    IntentResponseDto searchByText(int userId, String userQuery);
}
