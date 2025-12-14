package org.AiDiary.service;

import org.AiDiary.dto.request.TextRequestDto;
import org.AiDiary.dto.response.TextResponseDto;

public interface TextService {
    TextResponseDto createText(TextRequestDto textRequestDto);
}
