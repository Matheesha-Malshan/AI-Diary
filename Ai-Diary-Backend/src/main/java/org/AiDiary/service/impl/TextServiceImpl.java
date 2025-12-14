package org.AiDiary.service.impl;

import lombok.RequiredArgsConstructor;
import org.AiDiary.dto.request.TextRequestDto;
import org.AiDiary.dto.response.TextResponseDto;
import org.AiDiary.embedding.EmbeddingCreateService;
import org.AiDiary.service.TextService;
import org.AiDiary.validator.ValidationChain;
import org.AiDiary.vectorDb.SaveVector;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TextServiceImpl implements TextService {

    private final ModelMapper mapper;
    private final ValidationChain validationChain;
    private final EmbeddingCreateService embeddingCreateService;
    private final SaveVector saveVector;

    public TextResponseDto createText(TextRequestDto textRequestDto){

        var chain=validationChain.handleValidation(textRequestDto);




        return mapper.map(chain,TextResponseDto.class);





//        List<float[]> embeddingList=embeddingCreateService.createEmbeddings
//                (textRequestDto.getPromt());
//
//        var vectorEntity=saveVector.saveVectors(new VectorSavingDto());


    }
}
