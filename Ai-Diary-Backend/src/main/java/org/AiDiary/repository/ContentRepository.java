package org.AiDiary.repository;

import org.AiDiary.entity.ContentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.data.domain.Pageable;
import java.time.Instant;
import java.util.List;

public interface ContentRepository extends MongoRepository<ContentEntity,String> {

    List<ContentEntity> findByUserIdAndCreateDateBetween(int userid, Instant start,Instant end);

    List<ContentEntity> findByUserId(int userId, Pageable pageable);

}
