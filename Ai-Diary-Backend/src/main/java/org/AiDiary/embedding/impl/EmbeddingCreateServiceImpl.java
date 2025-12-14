package org.AiDiary.embedding.impl;

import org.AiDiary.embedding.EmbeddingCreateService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class EmbeddingCreateServiceImpl implements EmbeddingCreateService {

    public List<float[]> createEmbeddings(String query){
        return new ArrayList<>(5);
    }

}
