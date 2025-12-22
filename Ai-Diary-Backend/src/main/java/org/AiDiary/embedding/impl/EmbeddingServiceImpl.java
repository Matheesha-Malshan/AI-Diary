package org.AiDiary.embedding.impl;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Common;
import io.qdrant.client.grpc.JsonWithInt.Value;
import io.qdrant.client.grpc.Points;
import lombok.RequiredArgsConstructor;
import org.AiDiary.dto.request.EmbeddingSavingDto;
import org.AiDiary.dto.response.VectorSearchDto;
import org.AiDiary.embedding.EmbeddingService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class EmbeddingServiceImpl implements EmbeddingService {

    private final QdrantClient qdrantClient;
    private static final String COLLECTION_NAME = "Ai-diary";

    @Override
    public void saveEmbedding(EmbeddingSavingDto dto) {


        if (dto.getEmbedding() == null || dto.getEmbedding().length != 768) {
            throw new IllegalArgumentException("Embedding must be 768-dimensional");
        }

        if (dto.getChunk() == null || dto.getChunk().isBlank()) {
            throw new IllegalArgumentException("Chunk cannot be null or empty");
        }

        if (dto.getCreateDate() == null) {
            throw new IllegalArgumentException("Chunk cannot be null or empty");
        }

        Points.PointStruct point = toPoint(dto);

        Points.UpsertPoints request = Points.UpsertPoints.newBuilder()
                .setCollectionName(COLLECTION_NAME)
                .addPoints(point)
                .build();

        qdrantClient.upsertAsync(request);





    }
    private Points.PointStruct toPoint(EmbeddingSavingDto dto) {

        List<Float> vector = new ArrayList<>(dto.getEmbedding().length);
        for (float v : dto.getEmbedding()) {
            vector.add(v);
        }

        return Points.PointStruct.newBuilder()
                .setId(
                        Common.PointId.newBuilder()
                                .setUuid(UUID.randomUUID().toString())
                                .build()
                )
                .setVectors(
                        Points.Vectors.newBuilder()
                                .setVector(
                                        Points.Vector.newBuilder()
                                                .addAllData(vector)
                                                .build()
                                )
                                .build()
                )
                .putPayload("userId",
                        Value.newBuilder()
                                .setIntegerValue(dto.getUserId())
                                .build()
                )
                .putPayload("chunk",
                        Value.newBuilder()
                                .setStringValue(dto.getChunk())
                                .build()
                )
                .putPayload("createdAt",
                        Value.newBuilder()
                                .setStringValue(dto.getCreateDate().toString())
                                .build()
                )
                .build();
    }

    @Override
    public VectorSearchDto searchEmbeddings(float[] queryEmbedding, int userId, float threshold) {

        try {
            List<Float> vector = new ArrayList<>(queryEmbedding.length);
            for (float f : queryEmbedding) {
                vector.add(f);
            }

            Common.Filter filter = Common.Filter.newBuilder()
                    .addMust(
                            Common.Condition.newBuilder()
                                    .setField(
                                            Common.FieldCondition.newBuilder()
                                                    .setKey("userId")
                                                    .setMatch(
                                                            Common.Match.newBuilder()
                                                                    .setInteger(userId)
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();


            Points.SearchPoints searchRequest =
                    Points.SearchPoints.newBuilder()
                            .setCollectionName("Ai-diary")
                            .addAllVector(vector)
                            .setScoreThreshold(threshold)   // similarity threshold
                            .setLimit(50)                   // safety cap
                            .setFilter(filter)
                            .setWithPayload(
                                    Points.WithPayloadSelector.newBuilder()
                                            .setEnable(true)
                                            .build()
                            )
                            .build();

            // Execute search
            List<Points.ScoredPoint> results =
                    qdrantClient.searchAsync(searchRequest).get();

            List<String> chunkList = new ArrayList<>();
            List<LocalDate> dateList = new ArrayList<>();

            for (Points.ScoredPoint point : results) {

                var payload = point.getPayloadMap();

                // chunk
                if (payload.containsKey("chunk")) {
                    chunkList.add(payload.get("chunk").getStringValue());
                } else {
                    chunkList.add(null);
                }

                // createdAt
                if (payload.containsKey("createdAt")) {
                    String dateStr = payload.get("createdAt").getStringValue();
                    dateList.add(LocalDate.parse(dateStr));
                } else {
                    dateList.add(null);
                }
            }

            VectorSearchDto response = new VectorSearchDto();
            response.setUserId(userId);
            response.setChunkList(chunkList);
            response.setDateList(dateList);

            return response;

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error retrieving embeddings from Qdrant", e);
        }

    }


}
