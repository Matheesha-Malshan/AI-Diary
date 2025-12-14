package org.AiDiary.validator;

import org.AiDiary.dto.request.TextRequestDto;
import org.AiDiary.dto.response.TextValidatedDto;

public abstract class ValidationChain {

    public ValidationChain nextValidator;

    public TextValidatedDto handleValidation(TextRequestDto hadler){
        TextValidatedDto validity=isValid(hadler);

        if (!validity.isPresentError() && nextValidator!=null){
            return nextValidator.handleValidation(hadler);
        }
        return validity;
    }

    public abstract TextValidatedDto isValid(TextRequestDto object);
}
