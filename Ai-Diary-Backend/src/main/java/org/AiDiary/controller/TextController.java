package org.AiDiary.controller;

import lombok.RequiredArgsConstructor;
import org.AiDiary.dto.request.TextRequestDto;
import org.AiDiary.dto.response.IntentResponseDto;
import org.AiDiary.dto.response.TextResponseDto;
import org.AiDiary.service.TextService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/text")
@RequiredArgsConstructor
@CrossOrigin
public class TextController {

    private final TextService textService;

    @PostMapping("/create")
    public TextResponseDto createText(@RequestBody TextRequestDto textRequestDto){
        return textService.createText(textRequestDto);
    }

    @GetMapping("/find-by-text/{userId}/{userQuery}")
    public IntentResponseDto findText(@PathVariable int userId, @PathVariable String userQuery){
        return textService.searchByText(userId,userQuery);
    }

}
