package org.AiDiary.repository;

import org.AiDiary.model.ContentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContentRepository extends MongoRepository<ContentEntity,String> {
}
