package org.AiDiary.repository;

import org.AiDiary.model.UserChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChatRepository extends JpaRepository<UserChatEntity,Integer> {
}
