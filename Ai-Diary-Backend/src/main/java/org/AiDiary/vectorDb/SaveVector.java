package org.AiDiary.vectorDb;

import org.AiDiary.dto.request.VectorSavingDto;

public interface SaveVector {
    VectorSavingDto saveVectors(VectorSavingDto savingDto);
}
