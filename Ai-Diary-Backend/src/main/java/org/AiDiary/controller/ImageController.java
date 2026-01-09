package org.AiDiary.controller;

import lombok.RequiredArgsConstructor;
import org.AiDiary.dto.request.ImageRequestDto;
import org.AiDiary.dto.response.ImageResponseDto;
import org.AiDiary.service.ImageService;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/image")
@RestController
@CrossOrigin
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/create")
    public void createImage(@ModelAttribute ImageRequestDto imageRequestDto){
        imageService.createImage(imageRequestDto);
    }

    @GetMapping("/search-by-text/{userId}/{text}")
    public ImageResponseDto searchImageByText(@PathVariable int userId,@PathVariable String text){
        return imageService.searchImageByText(userId,text);
    }
}
