package org.AiDiary.llm;

import org.springframework.web.multipart.MultipartFile;

public interface VoiceToText {

    String createText(MultipartFile audioFile);
}
