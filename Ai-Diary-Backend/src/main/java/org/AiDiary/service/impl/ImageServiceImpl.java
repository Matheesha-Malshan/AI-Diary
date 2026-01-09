package org.AiDiary.service.impl;

import lombok.RequiredArgsConstructor;
import org.AiDiary.dto.request.EmbeddingSavingDto;
import org.AiDiary.dto.request.ImageRequestDto;
import org.AiDiary.dto.response.ImageResponseDto;
import org.AiDiary.dto.response.VectorSearchDto;
import org.AiDiary.embedding.EmbeddingCreateService;
import org.AiDiary.embedding.EmbeddingService;
import org.AiDiary.entity.ImageEntity;
import org.AiDiary.entity.UserEntity;
import org.AiDiary.exception.UserNotFoundException;
import org.AiDiary.file.ImageFile;
import org.AiDiary.repository.ImageRepository;
import org.AiDiary.repository.UserRepository;
import org.AiDiary.service.ImageService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final EmbeddingCreateService embeddingCreateService;
    private final EmbeddingService embeddingService;
    private final ImageFile imageFile;
    private final ModelMapper mapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createImage(ImageRequestDto imageRequestDto){

        try {
            UserEntity user=userRepository.findById(imageRequestDto.getUser_id())
                    .orElseThrow(NullPointerException::new);

            ImageEntity imageEntity=new ImageEntity();

            imageEntity.setCreateDate(imageRequestDto.getCreateDate());

            float[] embeddings=embeddingCreateService.createEmbeddings(imageRequestDto.getDescription());

            EmbeddingSavingDto embeddingSavingDto=new EmbeddingSavingDto();

            embeddingSavingDto.setEmbedding(embeddings);

            embeddingSavingDto.setChunk(imageRequestDto.getDescription());
            embeddingSavingDto.setCreateDate(imageRequestDto.getCreateDate());
            embeddingSavingDto.setUserId(imageRequestDto.getUser_id());

            user.addImages(imageEntity);
            imageEntity.setUserImageEntity(user);

            ImageEntity image=imageRepository.save(imageEntity);
            embeddingSavingDto.setEntryId(image.getId());

            embeddingService.saveEmbedding(embeddingSavingDto,"images");
            imageRequestDto.setEntryId(image.getId());

            String path=imageFile.saveImage(imageRequestDto);
            imageEntity.setImageUrl(path);

            imageRepository.save(imageEntity);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public ImageResponseDto searchImageByText(int userId,String text){

        try {
            float[] embeddings=embeddingCreateService.createEmbeddings(text);
            VectorSearchDto vectorSearchDto=embeddingService.searchEmbeddings(embeddings,userId,0.55f,"images");

            ImageResponseDto imageResponseDto=mapper.map(vectorSearchDto,ImageResponseDto.class);
            List<String> pathList=new ArrayList<>();


            for (int id: vectorSearchDto.getEntryId()){

                ImageEntity imageEntity=imageRepository.findById(id).orElseThrow();
                pathList.add(imageEntity.getImageUrl());
            }

            return imageFile.searchImage(pathList,imageResponseDto);

        } catch (Exception e) {
            throw new UserNotFoundException("user not found");
        }

    }


}
