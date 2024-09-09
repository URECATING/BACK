package com.uting.urecating.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="user")
public class SiteUser extends BaseEntity{
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

}
