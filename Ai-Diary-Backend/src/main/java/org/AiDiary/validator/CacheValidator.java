package org.AiDiary.validator;

import lombok.RequiredArgsConstructor;
import org.AiDiary.config.CacheConfig;
import org.AiDiary.dto.request.TextRequestDto;
import org.AiDiary.dto.response.TextValidatedDto;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Component
@RequiredArgsConstructor
public class CacheValidator extends ValidationChain{

    private final CacheConfig cache;

    @Override
    public TextValidatedDto isValid(TextRequestDto object) {

        TextValidatedDto textValidatedDto=new TextValidatedDto();

        lookUpCache(object,textValidatedDto);

        return textValidatedDto;

    }
    public void lookUpCache(TextRequestDto object,TextValidatedDto textValidatedDto){

        String[] arrayText=object.getPromt().split("[.]");

        for(String iText:arrayText){
            String hash= getHashCodeOfQuery(iText).concat(":"+object.getUserId());

            if (cache.getCache().getIfPresent(hash)!=null){
                textValidatedDto.setPresentError(true);
                textValidatedDto.setErrorType("you are repeating seme content");
                return;
            }
            cache.getCache().put(hash,"true");

        }
        
    }

    public String getHashCodeOfQuery(String text){

        try {
            MessageDigest digest=MessageDigest.getInstance("SHA-256");
            byte[] hashBytes=digest.digest(text.getBytes(StandardCharsets.UTF_8));

            StringBuilder stringBuilder=new StringBuilder();

            for (byte b:hashBytes){
                stringBuilder.append(String.format("%02x",b));
            }
            return stringBuilder.toString();


        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }


    }

}

