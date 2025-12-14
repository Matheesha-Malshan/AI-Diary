package org.AiDiary.validator;

import net.fellbaum.jemoji.EmojiManager;
import org.AiDiary.dto.request.TextRequestDto;
import org.AiDiary.dto.response.TextValidatedDto;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class ContentValidator extends ValidationChain{

    @Override
    public TextValidatedDto isValid(TextRequestDto object) {

        TextValidatedDto textValidatedDto=new TextValidatedDto();

        textValidatedDto.setUserQuery(object.getPromt());
        textValidatedDto.setPresentError(false);

        checkLengthOfCharacters(textValidatedDto);

        return textValidatedDto;
    }

    public void checkLengthOfCharacters(TextValidatedDto validatedDto){

        String text=validatedDto.getUserQuery();

        if (text==null ||text.isEmpty()){
            validatedDto.setPresentError(true);
            validatedDto.setErrorType("your content is Empty or null");
            return;
        }
        int textLength=text.length();

        if (textLength>150){
            validatedDto.setPresentError(true);
            validatedDto.setErrorType("your content is more than expected");
            return;
        }
        checkNullBytes(validatedDto);
    }
    public void checkNullBytes(TextValidatedDto validatedDto){

        String text=validatedDto.getUserQuery();

        if (text.indexOf('\0')!=-1){
            validatedDto.setPresentError(true);
            validatedDto.setErrorType("your content includes null bytes");
            return;
        }
        checkControlCharacters(validatedDto);
    }
    public void checkControlCharacters(TextValidatedDto validatedDto){

        String text=validatedDto.getUserQuery();

        Pattern pattern=Pattern.compile("\\p{Cntrl}");
        Matcher matcher=pattern.matcher(text);

        if (matcher.find()){
            validatedDto.setPresentError(true);
            validatedDto.setErrorType("your content has denied characters");
            return;
        }
        consecutiveRepeatCharacters(validatedDto);

    }
    public void consecutiveRepeatCharacters(TextValidatedDto validatedDto){

        String text=validatedDto.getUserQuery();

        Pattern pattern=Pattern.compile("([a-z])\\1{2,}");
        Matcher matcher=pattern.matcher(text);

        if (matcher.find()){
            validatedDto.setPresentError(true);
            validatedDto.setErrorType("your content has consecutive characters");
            return;
        }
        ApiPasswordDetection(validatedDto);
    }
   public void ApiPasswordDetection(TextValidatedDto validatedDto){

        String[] text=validatedDto.getUserQuery().split("[\\s,.]+");

        for (String result:text){
            if (result.length()>5 && result.matches(".*[A-Za-z].*")&&
                    result.matches(".*[0-9].*")){
                validatedDto.setPresentError(true);
                validatedDto.setErrorType("your content has password or un behaviour");
                return;
            }
        }
       detectLanguage(validatedDto);

   }
   public void detectLanguage(TextValidatedDto validatedDto){
       String text = validatedDto.getUserQuery();


       boolean hasOtherCharacters = text.codePoints()
                   .anyMatch(cp -> {
                   boolean isWhitespace = Character.isWhitespace(cp);
                   boolean isPunctuation = Character.getType(cp) == Character.OTHER_PUNCTUATION;
                   boolean isEnglishLetter =
                           (cp >= 'a' && cp <= 'z') ||
                                   (cp >= 'A' && cp <= 'Z');

                   boolean isNumber = Character.isDigit(cp);

                   boolean isEmoji = EmojiManager.isEmoji(String.valueOf(cp));

                   return !(isEnglishLetter || isNumber || isEmoji|| isWhitespace || isPunctuation);
               });

       if (hasOtherCharacters){
           validatedDto.setPresentError(true);
           validatedDto.setErrorType("your content has invalid characters. you can add" +
                   "English letters , numbers and Emojis only");

       }


   }


}
