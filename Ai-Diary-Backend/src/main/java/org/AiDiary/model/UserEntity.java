package org.AiDiary.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String name;
    private String email;

    @Column(name = "createdDate")
    private Date createdDate;

    @OneToMany(mappedBy = "userEntity",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<UserChatEntity> userChatEntities=new ArrayList<>();


    public void addChat(UserChatEntity userChat){
        userChatEntities.add(userChat);
        userChat.setUserEntity(this);
    }
}
