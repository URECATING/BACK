package com.uting.urecating.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostJoinDto {
    private Long id;
    private Long userId;
    private Long postId;
}

