package com.uting.urecating.dto;

import com.uting.urecating.domain.Category;
import com.uting.urecating.domain.Post;

import java.time.LocalDateTime;

public record PostDetailDto(Long userId, String username, String userImage, String team, String title, String content, String image, Category category, LocalDateTime meetingDate, String place, int maxCapacity, LocalDateTime createdAt, LocalDateTime updatedAt, boolean status) {

    public static PostDetailDto fromPost(Post p) {
        return new PostDetailDto(
                p.getUser().getId(),
                p.getUser().getUserName(),
                p.getUser().getImage(),
                p.getUser().getTeam(),
                p.getTitle(),
                p.getContent(),
                p.getImage(),
                p.getCategory(),
                p.getMeetingDate(),
                p.getPlace(),
                p.getMaxCapacity(),
                p.getCreatedAt(),
                p.getUpdatedAt(),
                p.isStatus());
    }
}
