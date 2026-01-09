package org.AiDiary.file;

import org.AiDiary.dto.request.ImageRequestDto;
import org.AiDiary.dto.response.ImageResponseDto;

import java.util.List;

public interface ImageFile {

    String saveImage(ImageRequestDto imageRequestDto);
    ImageResponseDto searchImage(List<String> pathList, ImageResponseDto response);
}
