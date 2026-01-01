package org.AiDiary.service;

import org.AiDiary.dto.request.VoiceRequestDto;
import org.AiDiary.dto.response.IntentResponseDto;
import org.AiDiary.dto.response.TextResponseDto;

public interface VoiceService {

    TextResponseDto createVoice(VoiceRequestDto voiceRequestDto);

}
