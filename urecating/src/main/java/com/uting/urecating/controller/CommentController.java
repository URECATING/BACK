package com.uting.urecating.controller;


import com.uting.urecating.config.exception.ApiException;
import com.uting.urecating.config.exception.ErrorCode;
import com.uting.urecating.config.exception.PostNotFoundException;
import com.uting.urecating.config.response.ApiResponse;
import com.uting.urecating.config.response.ResponseCode;
import com.uting.urecating.domain.SiteUser;
import com.uting.urecating.dto.CommentDto;
import com.uting.urecating.dto.CommentFieldDto;
import com.uting.urecating.dto.CommentReplyDto;
import com.uting.urecating.service.CommentService;
import com.uting.urecating.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;

    @GetMapping("/post/{post_id}/comments")
    public ResponseEntity<ApiResponse<List<CommentDto>>> getComments(@PathVariable Long post_id) {
        try{
        List<CommentDto> commentDto = commentService.getComments(post_id);
        ApiResponse<List<CommentDto>> response = new ApiResponse<>(ResponseCode.SUCCESS_SEARCH_COMMENT, commentDto);
        return ResponseEntity.status(response.getStatus()).body(response);
        }
        catch (IllegalArgumentException e){
            throw new ApiException(ErrorCode.COMMENT_SEARCH_ERROR);
        }
    }

    @PostMapping("/post/{post_id}/comments")
    public ResponseEntity<ApiResponse<CommentDto>> createComments(
            @PathVariable Long post_id, @RequestBody CommentFieldDto dto){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String login = authentication.getName();  // 로그인된 사용자의 아이디 (login 필드)

            SiteUser user = userService.findByLogin(login);

        CommentDto createDto = commentService.createComments(post_id, dto, user);
        ApiResponse<CommentDto> response = new ApiResponse<>(ResponseCode.SUCCESS_INSERT_COMMENT, createDto);
        return ResponseEntity.status(response.getStatus()).body(response);}
        catch (IllegalArgumentException e){
            throw new ApiException(ErrorCode.COMMENT_INSERT_ERROR);
        }
    }
    @PostMapping("/post/{parent_id}/comments/reply")
    public ResponseEntity<ApiResponse<CommentDto>> createReply(
            @PathVariable Long parent_id, @RequestBody CommentReplyDto dto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String login = authentication.getName();  // 로그인된 사용자의 아이디 (login 필드)

            SiteUser user = userService.findByLogin(login);

            CommentDto createDto = commentService.createReply(parent_id, dto, user);
            ApiResponse<CommentDto> response = new ApiResponse<>(ResponseCode.SUCCESS_INSERT_COMMENT, createDto);
            return ResponseEntity.status(response.getStatus()).body(response);
        } catch (IllegalArgumentException e) {
            throw new ApiException(ErrorCode.COMMENTREPLY_INSERT_ERROR);
        }
    }

    @PatchMapping("/comments/{id}")
    public ResponseEntity<ApiResponse<CommentDto>> update(@PathVariable Long id, @RequestBody CommentFieldDto dto){
        try{
        CommentDto updateDto = commentService.update(id, dto);
        ApiResponse<CommentDto> response = new ApiResponse<>(ResponseCode.SUCCESS_INSERT_COMMENT, updateDto);
        return ResponseEntity.status(response.getStatus()).body(response);}
        catch (IllegalArgumentException e){
            throw new ApiException(ErrorCode.COMMENT_UPDATE_ID_ERROR);
        }
    }


    @DeleteMapping("/comments/{id}")
    public ResponseEntity<ApiResponse<CommentDto>> delete(@PathVariable Long id){
        try {
            CommentDto deleteDto = commentService.delete(id);
            ApiResponse<CommentDto> response = new ApiResponse<>(ResponseCode.SUCCESS_DELETE_COMMENT, deleteDto);
            return ResponseEntity.status(response.getStatus()).body(response);
        }
        catch (IllegalArgumentException e){
            throw new ApiException(ErrorCode.COMMENT_DELETE_ERROR);
        }
    }
    @GetMapping("/{postId}/comment-count")
    public ResponseEntity<ApiResponse<Long>> getCommentCount(@PathVariable Long postId) {
        try {
        long count = commentService.getCommentCountByPostId(postId);
        ApiResponse<Long> response = new ApiResponse<>(ResponseCode.SUCCESS_COUNT_COMMENT, count);
        return ResponseEntity.status(response.getStatus()).body(response);
        }
        catch (PostNotFoundException e){
            throw new ApiException(ErrorCode.COMMENT_COUNT_POST_ERROR);
        }catch (IllegalArgumentException e){
            throw new ApiException(ErrorCode.COMMENT_COUNT_ERROR);
        }
    }
}



















