package com.uting.urecating.dto;


import com.uting.urecating.domain.Comment;
import com.uting.urecating.domain.Post;
import com.uting.urecating.domain.SiteUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class CommentDto {
    private Long id;
    private Long postId;
    private String content;
    private String username;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public static CommentDto createCommentDto(Comment comment, SiteUser user, Post post) {
        return new CommentDto(
                comment.getId(),
                post.getId(),
                comment.getContent(),
                user.getUserName(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }

}
