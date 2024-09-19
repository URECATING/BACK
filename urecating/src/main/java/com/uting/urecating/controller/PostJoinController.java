package com.uting.urecating.controller;

import com.uting.urecating.config.exception.ApiException;
import com.uting.urecating.config.exception.ErrorCode;
import com.uting.urecating.config.response.ApiResponse;
import com.uting.urecating.config.response.ResponseCode;
import com.uting.urecating.dto.PostJoinDto;
import com.uting.urecating.config.exception.PostNotFoundException;
import com.uting.urecating.config.exception.UserNotFoundException;
import com.uting.urecating.dto.PostJoinFieldDto;
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
    public ResponseEntity<ApiResponse<PostJoinFieldDto>> postJoin(@RequestBody PostJoinFieldDto dto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();
            Long userId = postJoinService.findUserIdByUsername(username); // 사용자 ID 찾기
            postJoinService.postJoin(userId, dto.getPostId());

            ApiResponse<PostJoinFieldDto> response = new ApiResponse<>(ResponseCode.SUCCESS_JOIN_POST, dto);
            return ResponseEntity.status(response.getStatus()).body(response);
        } catch (UserNotFoundException e) {
            // 사용자 정의 예외 클래스 사용 예시
            throw new ApiException(ErrorCode.POST_JOIN_USER_ERROR);
        } catch (PostNotFoundException e) {
            // 사용자 정의 예외 클래스 사용 예시
            throw new ApiException(ErrorCode.POST_JOIN_POST_ERROR);
        } catch (Exception e) {
            throw new ApiException(ErrorCode.POST_JOIN_ERROR);
        }
    }

    @GetMapping("/posts/{postId}/joins")
    public ResponseEntity<ApiResponse<List<PostJoinDto>>> getJoinByPost (@PathVariable Long postId) {
        try {
            List<PostJoinDto> joins = postJoinService.getJoinByPost(postId);
            ApiResponse<List<PostJoinDto>> response = new ApiResponse<>(ResponseCode.SUCCESS_JOIN_POST, joins);
            return ResponseEntity.status(response.getStatus()).body(response); // 수정예정
        }  catch (IllegalArgumentException e) {
            throw new ApiException(ErrorCode.POST_JOIN_SEARCH_POST_ERROR);
        }
    }

    @GetMapping("/users/{userId}/joins")
    public ResponseEntity<ApiResponse<List<PostJoinDto>>> getJoinByUser(@PathVariable Long userId) {
        try {
            List<PostJoinDto> joins = postJoinService.getJoinByUser(userId);
            ApiResponse<List<PostJoinDto>> response = new ApiResponse<>(ResponseCode.SUCCESS_JOIN_POST, joins);
            return ResponseEntity.status(response.getStatus()).body(response);// 수정예정
        } catch (IllegalArgumentException e) {
            throw new ApiException(ErrorCode.POST_JOIN_SEARCH_USER_ERROR);
        }
    }

    @DeleteMapping("/{joinId}")
    public ResponseEntity<ApiResponse<Void>> deleteJoinById(@PathVariable Long joinId) {
        try {
            postJoinService.deleteJoinById(joinId);
            ApiResponse<Void> response = new ApiResponse<>(ResponseCode.SUCCESS_DELETE_JOIN_POST, null);
            return ResponseEntity.status(response.getStatus()).body(response);// 수정예정
        } catch (IllegalArgumentException e) {
            throw new ApiException(ErrorCode.POST_JOIN_DELETE_USER_ERROR);
        }
    }
}