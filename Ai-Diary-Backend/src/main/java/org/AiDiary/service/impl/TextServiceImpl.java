package org.AiDiary.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.AiDiary.dto.request.EmbeddingSavingDto;
import org.AiDiary.dto.request.TextRequestDto;
import org.AiDiary.dto.response.TextResponseDto;
import org.AiDiary.dto.response.TextValidatedDto;
import org.AiDiary.embedding.EmbeddingCreateService;
import org.AiDiary.embedding.EmbeddingSavingService;
import org.AiDiary.llm.FeatureExtraction;
import org.AiDiary.model.ContentEntity;
import org.AiDiary.model.UserChatEntity;
import org.AiDiary.model.UserEntity;
import org.AiDiary.repository.ContentRepository;
import org.AiDiary.repository.UserRepository;
import org.AiDiary.service.TextService;
import org.AiDiary.validator.ValidationChain;
import org.AiDiary.vectorDb.SaveVector;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class TextServiceImpl implements TextService {

    private final ModelMapper mapper;
    private final ValidationChain validationChain;
    private final EmbeddingCreateService embeddingCreateService;
    private final SaveVector saveVector;
    private final FeatureExtraction featureExtraction;
    private final EmbeddingSavingService embeddingSavingService;
    private final ObjectMapper objectMapper;
    private final ContentRepository contentRepository;
    private final UserRepository userRepository;

    public TextResponseDto createText(TextRequestDto textRequestDto){

        TextValidatedDto chain=validationChain.handleValidation(textRequestDto);

        EmbeddingSavingDto embeddingSavingDto=new EmbeddingSavingDto();
        embeddingSavingDto.setUserId(textRequestDto.getUser_id());

        if (!chain.isPresentError()){

            String text=featureExtraction.featureExtractor(chain.getProccesedQuery());
            JsonNode jsonNode;

            try {
                jsonNode=objectMapper.readTree(text);
                log.info("string is converted to json object");

            } catch (JsonProcessingException e) {
                log.error("error on converting string into json");
                throw new RuntimeException(e);
            }

            String[] a=chain.getProccesedQuery().split("[.]");

            for(String chunk:a){
                float[] embeddings=embeddingCreateService.createEmbeddings(chunk);
                embeddingSavingDto.getEmbedding().add(embeddings);
                embeddingSavingDto.getChunk().add(chunk);
            }

            embeddingSavingService.saveEmbedding(embeddingSavingDto);
            log.info("data is saved on vector DB");

            double sentiment_score=jsonNode.findValue("sentiment_score").asDouble();

            UserEntity userEntity = userRepository.findById(textRequestDto.getUser_id())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + textRequestDto.getUser_id()));

            var userChat=mapper.map(textRequestDto, UserChatEntity.class);

            userChat.setSentimentScore(sentiment_score);

            userEntity.addChat(userChat);

            userRepository.save(userEntity);
            log.info("data is saved on EDB");

            JsonNode jsonKeyWords=jsonNode.get("features");

            ContentEntity content=new ContentEntity();

            content.setUserId(textRequestDto.getUser_id());
            content.setCreateDate(textRequestDto.getCreateDate());
            content.setUserPrompt(chain.getProccesedQuery());
            content.setPlaces(objectMapper.convertValue(jsonKeyWords.get("places"),
                    new TypeReference<>(){}));

            content.setPeople(objectMapper.convertValue(jsonKeyWords.get("people"),
                    new TypeReference<>(){}));

            content.setKeyWord(objectMapper.convertValue(jsonKeyWords.get("keyword"),
                    new TypeReference<>(){}));


            contentRepository.save(content);

            log.info("data is saved on mongo db");

        }
        return mapper.map(chain,TextResponseDto.class);

    }

    public String searchByText(int userId,String userQuery){
        return null;
    }
}
