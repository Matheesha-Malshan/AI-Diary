package org.AiDiary.embedding.impl;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Common;
import io.qdrant.client.grpc.JsonWithInt.Value;
import io.qdrant.client.grpc.Points;
import io.qdrant.client.grpc.Points.Vector;
import lombok.RequiredArgsConstructor;
import org.AiDiary.dto.request.EmbeddingSavingDto;
import org.AiDiary.embedding.EmbeddingSavingService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class EmbeddingSavingServiceImpl implements EmbeddingSavingService {

    private final QdrantClient qdrantClient;

    @Override
    public void saveEmbedding(EmbeddingSavingDto dto) {
        List<Points.PointStruct> points = new ArrayList<>();

        List<float[]> embeddings = dto.getEmbedding();
        List<String> chunks = dto.getChunk();

        // Ensure the sizes match
        if (embeddings.size() != chunks.size()) {
            throw new IllegalArgumentException("Embeddings and chunks size must match for userId: " + dto.getUserId());
        }

        for (int i = 0; i < embeddings.size(); i++) {
            points.add(toPoint(dto.getUserId(), embeddings.get(i), chunks.get(i)));
        }

        Points.UpsertPoints request = Points.UpsertPoints.newBuilder()
                .setCollectionName("Ai-diary")
                .addAllPoints(points)
                .build();

        // Save asynchronously
        qdrantClient.upsertAsync(request);
    }

    private Points.PointStruct toPoint(int userId, float[] embedding, String chunk) {
        // Payload
        Map<String, Value> payload = new HashMap<>();
        payload.put("userId", Value.newBuilder().setIntegerValue(userId).build());
        payload.put("chunk", Value.newBuilder().setStringValue(chunk).build());

        // Vector
        List<Float> vector = new ArrayList<>(embedding.length);
        for (float f : embedding) {
            vector.add(f);
        }

        return Points.PointStruct.newBuilder()
                .setId(Common.PointId.newBuilder()
                        .setUuid(UUID.randomUUID().toString())
                        .build())
                .setVectors(Points.Vectors.newBuilder()
                        .setVector(Vector.newBuilder().addAllData(vector).build())
                        .build())
                .putAllPayload(payload)
                .build();
    }
}
