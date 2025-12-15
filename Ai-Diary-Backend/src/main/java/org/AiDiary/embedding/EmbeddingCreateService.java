package org.AiDiary.embedding;

import reactor.core.publisher.Mono;

import java.util.List;

public interface EmbeddingCreateService {
    float[] createEmbeddings(String query);
}
