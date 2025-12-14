package org.AiDiary.config;

import org.AiDiary.validator.ContentRepair;
import org.AiDiary.validator.ContentValidator;
import org.AiDiary.validator.CacheValidator;
import org.AiDiary.validator.ValidationChain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidationChainConfig {

    @Bean
    public ValidationChain validationChain(ContentValidator contentValidator,
                                           CacheValidator cacheValidator, ContentRepair contentRepair){

        contentValidator.nextValidator=cacheValidator;
        cacheValidator.nextValidator=contentRepair;
        return contentValidator;
    }
}
