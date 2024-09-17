package com.uting.urecating.controller;

import com.uting.urecating.config.exception.ApiException;
import com.uting.urecating.config.exception.ErrorCode;
import com.uting.urecating.config.response.ApiResponse;
import com.uting.urecating.config.response.ResponseCode;
import com.uting.urecating.domain.PostJoin;
import com.uting.urecating.domain.SiteUser;
import com.uting.urecating.dto.PostJoinDto;
import com.uting.urecating.service.PostJoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post-join")
public class PostJoinController {

    private final PostJoinService postJoinService;

    @PostMapping
    public ResponseEntity<ApiResponse<PostJoinDto>> postJoin(@RequestBody PostJoinDto dto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();
            Long userId = postJoinService.findUserIdByUsername(username); // 사용자 ID 찾기
            postJoinService.postJoin(userId, dto.getPostId());

            ApiResponse<PostJoinDto> response = new ApiResponse<>(ResponseCode.SUCCESS_JOIN_POST, null);
            return ResponseEntity.status(response.getStatus()).body(response);
        } catch (IllegalArgumentException e) {
            throw new ApiException(ErrorCode.POST_JOIN_ERROR);
        }
    }

    @GetMapping("/posts/{postId}/joins")
    public ResponseEntity<List<PostJoinDto>> getJoinByPost(@PathVariable Long postId) {
            List<PostJoinDto> joins = postJoinService.getJoinByPost(postId);
            return ResponseEntity.ok(joins); // 수정예정
    }

    @GetMapping("/users/{userId}/joins")
    public ResponseEntity<List<PostJoinDto>> getJoinByUser(@PathVariable Long userId) {
        List<PostJoinDto> joins = postJoinService.getJoinByUser(userId);
        return ResponseEntity.ok(joins);// 수정예정
    }

    @DeleteMapping("/{joinId}")
    public ResponseEntity<Void> deleteJoinById(@PathVariable Long joinId) {
        postJoinService.deleteJoinById(joinId);
        return ResponseEntity.noContent().build();// 수정예정
    }
}