package org.AiDiary.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDate;


@Entity
@Setter
@Getter
@Table(name = "user_chat")
public class UserChatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Column(name = "createDate")
    private LocalDate createDate;

    @Column(name = "sentimentScore")
    private double sentimentScore;

}
