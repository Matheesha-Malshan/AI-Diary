package org.AiDiary.repository;

import org.AiDiary.entity.UserChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface UserChatRepository extends JpaRepository<UserChatEntity,Integer> {

    Optional<UserChatEntity> findFirstByUserEntity_UserIdOrderBySentimentScoreDescCreateDateDesc(int userId);

    List<UserChatEntity> findAllByUserEntity_UserIdOrderBySentimentScoreDescCreateDateDesc(int userId);

    Optional<UserChatEntity> findFirstByUserEntity_UserIdOrderBySentimentScoreAscCreateDateDesc(int userId);

    @Query(
            value = """
        SELECT YEAR(createDate) AS year,
               WEEK(createDate) AS week,
               AVG(sentimentScore) AS avgSentiment
        FROM user_chat
        WHERE user_id = :userId
        GROUP BY year, week
        ORDER BY avgSentiment DESC
        LIMIT 1
        """,
            nativeQuery = true
    )
    Map<String, Object> findHappiestWeek(@Param("userId") int userId);


    @Query(
            value = """
        SELECT YEAR(createDate) AS year,
               MONTH(createDate) AS month,
               AVG(sentimentScore) AS avgSentiment
        FROM user_chat
        WHERE user_id = :userId
        GROUP BY year, month
        ORDER BY avgSentiment DESC
        LIMIT 1
        """,
            nativeQuery = true
    )
    Map<String, Object> findHappiestMonth(@Param("userId") int userId);


    @Query(
            value = """
        SELECT createDate, sentimentScore,
               ABS(sentimentScore - 0.3) AS emotionalIntensity
        FROM user_chat
        WHERE user_id = :userId
        ORDER BY emotionalIntensity DESC, createDate DESC
        LIMIT 1
        """,
            nativeQuery = true
    )
    Map<String, Object> findMostEmotionalDay(@Param("userId") int userId);








}
