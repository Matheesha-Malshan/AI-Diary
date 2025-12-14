package org.AiDiary.vectorDb.impl;

import org.AiDiary.dto.request.VectorSavingDto;
import org.AiDiary.vectorDb.SaveVector;
import org.springframework.stereotype.Component;

@Component
public class SaveVectorimpl implements SaveVector {

    public VectorSavingDto saveVectors(VectorSavingDto savingDto){
        return new VectorSavingDto();
    }
}
