package com.uting.urecating.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentFieldDto {
    @Schema(description = "댓글 내용", example = "이것은 댓글입니다.", required = true)
    private String content;
}