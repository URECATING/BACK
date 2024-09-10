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

    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 실제 권한 리스트를 반환해야 합니다.
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")); // 예시
    }
}
