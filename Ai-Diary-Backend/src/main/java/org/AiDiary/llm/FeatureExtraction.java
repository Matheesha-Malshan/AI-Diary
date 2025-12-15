package org.AiDiary.llm;

import reactor.core.publisher.Mono;

public interface FeatureExtraction {
    String featureExtractor(String userMessage);
}
