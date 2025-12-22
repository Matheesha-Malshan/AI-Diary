package org.AiDiary.embedding;


import org.AiDiary.dto.request.EmbeddingSavingDto;
import org.AiDiary.dto.response.VectorSearchDto;

import java.util.List;

public interface EmbeddingService {
    void saveEmbedding(EmbeddingSavingDto dto);
    VectorSearchDto searchEmbeddings(float[] queryEmbedding, int userId, float threshold);
}
