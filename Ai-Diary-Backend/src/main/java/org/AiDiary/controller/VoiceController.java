package org.AiDiary.controller;

import lombok.RequiredArgsConstructor;
import org.AiDiary.dto.request.VoiceRequestDto;
import org.AiDiary.dto.response.IntentResponseDto;
import org.AiDiary.dto.response.TextResponseDto;
import org.AiDiary.service.VoiceService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/voice")
@CrossOrigin
public class VoiceController {

    private final VoiceService voiceService;

    @PostMapping("/create")
    public TextResponseDto createVoice(@ModelAttribute VoiceRequestDto voiceRequestDto){
        return voiceService.createVoice(voiceRequestDto);
    }

}
