package org.AiDiary.embedding;

import java.util.List;

public interface EmbeddingCreateService {
    List<float[]> createEmbeddings(String query);
}
