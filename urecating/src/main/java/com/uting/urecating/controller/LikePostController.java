package com.uting.urecating.controller;


import com.uting.urecating.config.exception.ApiException;
import com.uting.urecating.config.exception.ErrorCode;
import com.uting.urecating.config.exception.PostNotFoundException;
import com.uting.urecating.config.exception.UserNotFoundException;
import com.uting.urecating.config.response.ApiResponse;
import com.uting.urecating.config.response.ResponseCode;
import com.uting.urecating.domain.LikePost;
import com.uting.urecating.domain.SiteUser;
import com.uting.urecating.dto.LikePostDto;
import com.uting.urecating.service.LikePostService;
import com.uting.urecating.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class LikePostController {
    private final LikePostService likePostService;
    private final UserService userService;

    @PostMapping("/post/{post_id}/like")
    public ResponseEntity<ApiResponse<Void>> addLikePost(@PathVariable Long post_id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String login = authentication.getName();  // 로그인된 사용자의 아이디 (login 필드)

            SiteUser user = userService.findByLogin(login);

            likePostService.addLikePost(user.getId(), post_id);
            ApiResponse<Void> response = new ApiResponse<>(ResponseCode.SUCCESS_LIKE_POST, null);
            return ResponseEntity.status(response.getStatus()).body(response);
        }  catch (UserNotFoundException e) {
            throw new ApiException(ErrorCode.LIKE_POST_USER_ERROR);
        } catch (PostNotFoundException e) {
            throw new ApiException(ErrorCode.LIKE_POST_POST_ERROR);
        }catch (IllegalArgumentException e ){
            throw new ApiException(ErrorCode.LIKE_POST_DOUBLE_ERROR);
        }
        catch (Exception e) {
            throw new ApiException(ErrorCode.POST_JOIN_ERROR);
        }
    }

    // 좋아요 취소
    @DeleteMapping("/post/{post_id}/like")
    public ResponseEntity<ApiResponse<Void>> removeLikePost(@PathVariable Long post_id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String login = authentication.getName();  // 로그인된 사용자의 아이디 (login 필드)

            SiteUser user = userService.findByLogin(login);
            likePostService.removeLikePost(user.getId(), post_id);
            ApiResponse<Void> response = new ApiResponse<>(ResponseCode.SUCCESS_DELETE_LIKE_POST, null);
            return ResponseEntity.status(response.getStatus()).body(response);
        }  catch (UserNotFoundException e) {
            throw new ApiException(ErrorCode.LIKE_POST_USER_DELETE_ERROR);
        } catch (PostNotFoundException e) {
            throw new ApiException(ErrorCode.LIKE_POST_POST_DELETE_ERROR);
        } catch (IllegalArgumentException e) {
            throw new ApiException(ErrorCode.LIKE_POST_DELETE_ERROR);
        }
    }

    @GetMapping("/post/{post_id}/is-liked")
    public ResponseEntity<ApiResponse<Boolean>> isPostLikedByUser(@PathVariable Long post_id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String login = authentication.getName();  // 로그인된 사용자의 아이디 (login 필드)

            SiteUser user = userService.findByLogin(login);

            boolean isLiked = likePostService.isPostLikedByUser(user.getId(), post_id);
            ApiResponse<Boolean> response = new ApiResponse<>(ResponseCode.SUCCESS_CHECK_LIKE_POST, isLiked);
            return ResponseEntity.status(response.getStatus()).body(response);
        }  catch (UserNotFoundException e) {
            throw new ApiException(ErrorCode.LIKE_POST_USER_CHECK_ERROR);
        } catch (PostNotFoundException e) {
            throw new ApiException(ErrorCode.LIKE_POST_POST_CHECK_ERROR);
        } catch (IllegalArgumentException e) {
            throw new ApiException(ErrorCode.LIKE_POST_CHECK_ERROR);
        }
    }

    @GetMapping("/user/likes")
    public ResponseEntity<ApiResponse<List<LikePostDto>>> getLikesByUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String login = authentication.getName();  // 로그인된 사용자의 아이디 (login 필드)

            SiteUser user = userService.findByLogin(login);

            List<LikePost> likedPosts = likePostService.getLikesByUser(user.getId());
            List<LikePostDto> likedPostDtos = likedPosts.stream()
                    .map(likePost -> new LikePostDto(
                            likePost.getId(),                   // ID of the LikePost
                            likePost.getUser().getId(),        // User ID who liked the post
                            likePost.getPost().getId()
                    ))
                    .collect(Collectors.toList());

            ApiResponse<List<LikePostDto>> response = new ApiResponse<>(ResponseCode.SUCCESS_SEARCH_LIKE_POST, likedPostDtos);
            return ResponseEntity.status(response.getStatus()).body(response);
        } catch (UserNotFoundException e) {
            throw new ApiException(ErrorCode.LIKE_POST_USER_SEARCH_ERROR);
        }catch (IllegalArgumentException e) {
            throw new ApiException(ErrorCode.LIKE_POST_SEARCH_ERROR);
        }
    }

}
