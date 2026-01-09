package org.AiDiary.validator;

import org.AiDiary.dto.request.TextRequestDto;
import org.AiDiary.dto.response.TextValidatedDto;
import org.springframework.stereotype.Component;

@Component
public class ContentRepair extends ValidationChain{

    @Override
    public TextValidatedDto isValid(TextRequestDto object) {

        TextValidatedDto textValidatedDto=new TextValidatedDto();
        textValidatedDto.setUserQuery(object.getPromt());

        removeWhiteSpaces(textValidatedDto);
        return textValidatedDto;
    }
    private void removeWhiteSpaces(TextValidatedDto textValidatedDto){

        textValidatedDto.setProccesedQuery(textValidatedDto.getUserQuery().trim().replaceAll("\\s+", " "));
    }

}
