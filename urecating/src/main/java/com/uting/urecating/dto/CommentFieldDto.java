package com.uting.urecating.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentFieldDto {
    @Schema(description = "댓글 내용", example = "이것은 댓글입니다.", required = true)
    private String content;
}