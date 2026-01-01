package org.AiDiary.service;

import org.AiDiary.dto.request.ImageRequestDto;
import org.AiDiary.dto.response.ImageResponseDto;

public interface ImageService {

    void createImage(ImageRequestDto imageRequestDto);
    ImageResponseDto searchImageByText(int userId, String text);
}
