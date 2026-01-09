package org.AiDiary.repository;

import org.AiDiary.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<ImageEntity,Integer> {
    List<ImageEntity> findByUserImageEntity_UserId(Integer userId);
}
