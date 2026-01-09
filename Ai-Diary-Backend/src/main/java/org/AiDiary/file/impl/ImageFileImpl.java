package org.AiDiary.file.impl;

import org.AiDiary.dto.request.ImageRequestDto;
import org.AiDiary.dto.response.ImageResponseDto;
import org.AiDiary.file.ImageFile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

@Service
public class ImageFileImpl implements ImageFile {

    @Override
    public String saveImage(ImageRequestDto imageRequestDto){

        String path = "./././././wwwroot/"+imageRequestDto.getUser_id();

        File directory=new File(path);

        if (directory.exists()){
            Path imagePath= Paths.get(path+"/"+imageRequestDto.getEntryId());
            try {

                Files.write(imagePath,imageRequestDto.getImage().getBytes());

                return imagePath.toString();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            try {
                Path paths=Files.createDirectory(Path.of(path));
                Path imagePath=Paths.get(paths+"/"+imageRequestDto.getEntryId());

                Files.write(imagePath,imageRequestDto.getImage().getBytes());

                return imagePath.toString();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Override
    public ImageResponseDto searchImage(List<String> pathList, ImageResponseDto response){

        for(String paths:pathList){
            Path path=Paths.get(paths);

            try {
                byte[] content=Files.readAllBytes(path);
                response.getImage().add(Base64.getEncoder().encodeToString(content));


            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        return response;

    }



}
