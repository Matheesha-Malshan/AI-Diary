package org.AiDiary.config;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QdrantClientConfig {

    @Value("${mlService.api.qUrl}")
    private String qUrl;

    @Value("${qdrant.port:6334}")
    private int port;

    @Bean
    public QdrantClient qdrantClient(){
        QdrantGrpcClient grpcClient = QdrantGrpcClient.newBuilder(qUrl, port, false)
                .build();

        return new QdrantClient(grpcClient);
    }
}
