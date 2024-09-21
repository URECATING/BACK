package com.uting.urecating.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

@Entity
@Getter
@AllArgsConstructor
@Builder
@Table(name="user")
public class SiteUser extends BaseEntity {
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

    @Column(name = "user_image", columnDefinition = "TEXT")
    private String image; // 사용자 이미지 URL

    @Column(nullable = false)
    private String phone; //전화번호

    @Column(nullable = false)
    private String team; //소속

    @Column(nullable = false)
    private String gender; //회원 성별

    private static final String DEFAULT_IMAGE_URL = "https://urecating.s3.ap-northeast-2.amazonaws.com/urecating_profile_default.jpg";

    // 기본 생성자에서 기본 이미지 URL 설정
    public SiteUser() {
        this.image = DEFAULT_IMAGE_URL;
    }

    public String getImage() {
        return (image == null || image.isEmpty()) ? DEFAULT_IMAGE_URL : image;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("USER")); // 예시
    }
}
