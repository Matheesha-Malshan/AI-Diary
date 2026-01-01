package org.AiDiary.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
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
    private Integer userId;
    private String name;
    private String email;

    @Column(name = "createdDate")
    private LocalDate createdDate;

    @OneToMany(mappedBy = "userEntity",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<UserChatEntity> userChatEntities=new ArrayList<>();

    @OneToMany(mappedBy = "userVoice",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<VoiceEntity> userVoice=new ArrayList<>();

    @OneToMany(mappedBy = "userImageEntity",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageEntity> imageEntities=new ArrayList<>();

    public void addChat(UserChatEntity userChat){
        userChatEntities.add(userChat);

    }
    public void addVoice(VoiceEntity voice){
        userVoice.add(voice);
    }

    public void addImages(ImageEntity image){
        imageEntities.add(image);
    }
}
