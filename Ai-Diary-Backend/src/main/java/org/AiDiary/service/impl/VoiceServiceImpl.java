package org.AiDiary.service.impl;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.AiDiary.dto.request.EmbeddingSavingDto;
import org.AiDiary.dto.request.TextRequestDto;
import org.AiDiary.dto.request.VoiceRequestDto;
import org.AiDiary.dto.response.TextResponseDto;
import org.AiDiary.dto.response.TextValidatedDto;
import org.AiDiary.embedding.EmbeddingCreateService;
import org.AiDiary.embedding.EmbeddingService;
import org.AiDiary.entity.ContentEntity;
import org.AiDiary.entity.UserChatEntity;
import org.AiDiary.entity.UserEntity;
import org.AiDiary.llm.FeatureExtraction;
import org.AiDiary.llm.VoiceToText;
import org.AiDiary.repository.ContentRepository;
import org.AiDiary.repository.UserChatRepository;
import org.AiDiary.repository.UserRepository;
import org.AiDiary.service.VoiceService;
import org.AiDiary.validator.ValidationChain;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoiceServiceImpl implements VoiceService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final VoiceToText voiceToText;
    private final ValidationChain validationChain;
    private final FeatureExtraction featureExtraction;
    private final ObjectMapper objectMapper;
    private final UserChatRepository userChatRepository;
    private final ContentRepository contentRepository;
    private final EmbeddingCreateService embeddingCreateService;
    private final EmbeddingService embeddingService;

    @Override
    public TextResponseDto createVoice(VoiceRequestDto voiceRequestDto){


        String texts=voiceToText.createText(voiceRequestDto.getAudio());

        TextRequestDto textRequestDto=new TextRequestDto();
        textRequestDto.setPromt(texts);
        textRequestDto.setUser_id(voiceRequestDto.getUserId());
        textRequestDto.setCreateDate(voiceRequestDto.getCreateDate());

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
                content.setCreateDate(textRequestDto.getCreateDate()
                        .atTime(LocalTime.NOON).atZone(ZoneId.systemDefault()).toInstant());
                content.setUserPrompt(chain.getProccesedQuery());

                ContentEntity contents= contentRepository.save(content);//save in mongo db
                contentId=contents.getId();

                try {
                    float[] embeddings=embeddingCreateService.createEmbeddings(chain.getProccesedQuery()); //http call

                    EmbeddingSavingDto embeddingSavingDto=mapper.map(textRequestDto,EmbeddingSavingDto.class);

                    embeddingSavingDto.setUserId(textRequestDto.getUser_id());
                    embeddingSavingDto.setEmbedding(embeddings);
                    embeddingSavingDto.setChunk(chain.getProccesedQuery());
                    embeddingSavingDto.setEntryId(userChats.getId());

                    embeddingService.saveEmbedding(embeddingSavingDto,"Ai-diary");

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
        return mapper.map(chain, TextResponseDto.class);

    }

}
