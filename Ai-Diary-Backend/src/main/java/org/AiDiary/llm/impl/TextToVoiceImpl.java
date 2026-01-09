package org.AiDiary.llm.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.AiDiary.llm.VoiceToText;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class TextToVoiceImpl implements VoiceToText {

    private final RestTemplate restTemplate;

    @Value("${mlService.api.urlV}")
    private String url;

    @Value("${mlService.api.key}")
    private String apiKey;


    public TextToVoiceImpl(@Qualifier("voiceTemplate") RestTemplate template){
        restTemplate=template;
    }

    @Override
    public String createText(MultipartFile audioFile) {

        ByteArrayResource arrayResource;

        try {
            byte[] audioByte=audioFile.getBytes();
            arrayResource=new ByteArrayResource(audioByte){

                @Override
                public String getFilename() {
                    return audioFile.getOriginalFilename();
                }
            };

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        MultiValueMap<String,Object> multiValueMap=new LinkedMultiValueMap<>();

        multiValueMap.add("file",arrayResource);
        multiValueMap.add("model","whisper-large-v3");
        multiValueMap.add("response_format","verbose_json");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBearerAuth(apiKey);

        HttpEntity<MultiValueMap<String, Object>> request =
                new HttpEntity<>(multiValueMap, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                String.class
        );

        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = null;
        try {
            json = mapper.readTree(response.getBody());
            return json.path("text").asText();

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }






    }
}
