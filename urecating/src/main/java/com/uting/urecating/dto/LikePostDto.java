package com.uting.urecating.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class LikePostDto {
    private Long id;
    private Long userid;
    private Long postId;
}
