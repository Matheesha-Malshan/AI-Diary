package org.AiDiary.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.AiDiary.dto.request.EmbeddingSavingDto;
import org.AiDiary.dto.request.TextRequestDto;
import org.AiDiary.dto.response.TextResponseDto;
import org.AiDiary.dto.response.TextValidatedDto;
import org.AiDiary.dto.response.VectorSearchDto;
import org.AiDiary.embedding.EmbeddingCreateService;
import org.AiDiary.embedding.EmbeddingService;
import org.AiDiary.llm.FeatureExtraction;
import org.AiDiary.llm.QuerySelector;
import org.AiDiary.model.ContentEntity;
import org.AiDiary.model.UserChatEntity;
import org.AiDiary.model.UserEntity;
import org.AiDiary.repository.ContentRepository;
import org.AiDiary.repository.UserChatRepository;
import org.AiDiary.repository.UserRepository;
import org.AiDiary.service.TextService;
import org.AiDiary.validator.ValidationChain;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




@Slf4j
@RequiredArgsConstructor
@Service
public class TextServiceImpl implements TextService {

    private final ModelMapper mapper;
    private final ValidationChain validationChain;
    private final EmbeddingCreateService embeddingCreateService;
    private final FeatureExtraction featureExtraction;
    private final EmbeddingService embeddingService;
    private final ObjectMapper objectMapper;
    private final ContentRepository contentRepository;
    private final UserRepository userRepository;
    private final UserChatRepository userChatRepository;
    private final QuerySelector querySelector;

    @Transactional
    public TextResponseDto createText(TextRequestDto textRequestDto){

        TextValidatedDto chain=validationChain.handleValidation(textRequestDto);
        String contentId;
        if (!chain.isPresentError()){

            try {
                String text=featureExtraction.featureExtractor(chain.getProccesedQuery()); //http call

                JsonNode jsonNode = objectMapper.readTree(text);

                log.info("string is converted to json object");

                double sentiment_score=jsonNode.findValue("sentiment_score").asDouble();

                UserEntity userEntity = userRepository.findById(textRequestDto.getUser_id())
                        .orElseThrow(() -> new RuntimeException("User not found with id: " + textRequestDto.getUser_id()));

                var userChat=mapper.map(textRequestDto, UserChatEntity.class);

                userChat.setSentimentScore(sentiment_score);

                userEntity.addChat(userChat);
                userChat.setUserEntity(userEntity);

                UserChatEntity userChats=userChatRepository.save(userChat); //save sql db

                if (userChats.getId()==null){
                    throw new RuntimeException("error creating userId");
                }

                ContentEntity content=new ContentEntity();

                content.setId(userChats.getId().toString());
                content.setUserId(textRequestDto.getUser_id());
                content.setCreateDate(textRequestDto.getCreateDate());
                content.setUserPrompt(chain.getProccesedQuery());

                ContentEntity contents= contentRepository.save(content);//save in mongo db
                contentId=contents.getId();

                try {
                    float[] embeddings=embeddingCreateService.createEmbeddings(chain.getProccesedQuery()); //http call

                    EmbeddingSavingDto embeddingSavingDto=mapper.map(textRequestDto,EmbeddingSavingDto.class);

                    embeddingSavingDto.setUserId(textRequestDto.getUser_id());
                    embeddingSavingDto.setEmbedding(embeddings);
                    embeddingSavingDto.setChunk(chain.getProccesedQuery());

                    embeddingService.saveEmbedding(embeddingSavingDto);

                } catch (Exception e) {

                    if (contentId!=null){
                        contentRepository.deleteById(contentId);
                    }
                    throw new RuntimeException("mongo db rollback fail",e);
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        return mapper.map(chain,TextResponseDto.class);

    }

    public String searchByText(int userId,String userQuery){

        float[] arr=embeddingCreateService.createEmbeddings(userQuery);

        VectorSearchDto strings=embeddingService.searchEmbeddings(arr,userId,0.62f);
        System.out.println(strings.getChunkList());
        System.out.println(strings.getDateList());
        return null;
    }
}
