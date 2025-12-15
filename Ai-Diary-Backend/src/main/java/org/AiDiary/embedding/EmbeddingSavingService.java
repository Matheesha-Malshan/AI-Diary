package org.AiDiary.embedding;


import org.AiDiary.dto.request.EmbeddingSavingDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface EmbeddingSavingService {
    void saveEmbedding(EmbeddingSavingDto dto);
}
