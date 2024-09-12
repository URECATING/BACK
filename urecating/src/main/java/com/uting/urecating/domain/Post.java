package com.uting.urecating.domain;

import com.uting.urecating.dto.PostUpdateDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private SiteUser user;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "Text", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private LocalDateTime meetingDate;

    private String place;

    @Column(nullable = false)
    private int maxCapacity;

    @Column(nullable = false)
    private boolean status;

    public Post(SiteUser user, String title, String content, Category category, LocalDateTime meetingDate, String place, int maxCapacity, boolean status) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.category = category;
        this.meetingDate = meetingDate;
        this.place = place;
        this.maxCapacity = maxCapacity;
        this.status = status;
    }

    public void update(PostUpdateDto p){
        this.title = p.title();
        this.content = p.content();
        this.category = p.category();
        this.meetingDate = p.meetingDate();
        this.place = p.place();
        this.maxCapacity = p.maxCapacity();
    }
}
