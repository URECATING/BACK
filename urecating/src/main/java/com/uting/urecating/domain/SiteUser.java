package com.uting.urecating.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter @Getter
@Table(name="user")
public class SiteUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")

    private Long id;

    @Column(unique = true, nullable = false)
    private String login; //아이디

    @Column(nullable=false)
    private String password; //비밀번호

    @Column(name = "user_name", nullable = false)
    private String userName; //이름

    @Column(name="user_image", columnDefinition = "TEXT")
    private String image; //이미지url

    @Column(nullable = false)
    private String phone; //전화번호

    @Column(nullable = false)
    private String team; //소속

    @Column(nullable = false)
    private String gender; //회원 성별

    @Column(name = "created_at")
    private LocalDateTime createdAt; //생성날짜

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; //수정날짜

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void onPreUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

}
