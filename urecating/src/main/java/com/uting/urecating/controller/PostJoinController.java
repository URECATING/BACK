package com.uting.urecating.controller;

import com.uting.urecating.config.exception.ApiException;
import com.uting.urecating.config.exception.ErrorCode;
import com.uting.urecating.config.response.ApiResponse;
import com.uting.urecating.config.response.ResponseCode;
import com.uting.urecating.domain.SiteUser;
import com.uting.urecating.dto.PostJoinDto;
import com.uting.urecating.config.exception.PostNotFoundException;
import com.uting.urecating.config.exception.UserNotFoundException;
import com.uting.urecating.dto.PostJoinFieldDto;
import com.uting.urecating.service.PostJoinService;
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
@RequestMapping("/post-join")
public class PostJoinController {

    private final PostJoinService postJoinService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<PostJoinFieldDto>> postJoin(@RequestBody PostJoinFieldDto dto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String login = authentication.getName();  // 로그인된 사용자의 아이디 (login 필드)

            // 로그인 아이디를 통해 SiteUser 엔티티를 조회하고, 해당 사용자의 ID를 가져옴
            SiteUser siteUser = userService.findByLogin(login);
            Long userId = siteUser.getId();
            postJoinService.postJoin(userId, dto.getPostId());

            ApiResponse<PostJoinFieldDto> response = new ApiResponse<>(ResponseCode.SUCCESS_JOIN_POST, dto);
            return ResponseEntity.status(response.getStatus()).body(response);
        } catch (UserNotFoundException e) {
            throw new ApiException(ErrorCode.POST_JOIN_USER_ERROR);
        } catch (PostNotFoundException e) {
            throw new ApiException(ErrorCode.POST_JOIN_POST_ERROR);
        }catch (IllegalArgumentException e ){
            throw new ApiException(ErrorCode.POST_JOIN_DOUBLE_ERROR);
        }
        catch (Exception e) {
            throw new ApiException(ErrorCode.POST_JOIN_ERROR);
        }
    }

    @GetMapping("/posts/{postId}/joins")
    public ResponseEntity<ApiResponse<List<PostJoinDto>>> getJoinByPost (@PathVariable Long postId) {
        try {
            List<PostJoinDto> joins = postJoinService.getJoinByPost(postId);
            ApiResponse<List<PostJoinDto>> response = new ApiResponse<>(ResponseCode.SUCCESS_SEARCH_JOIN_POST, joins);
            return ResponseEntity.status(response.getStatus()).body(response);
        }  catch (IllegalArgumentException e) {
            throw new ApiException(ErrorCode.POST_JOIN_SEARCH_POST_ERROR);
        }
    }

    @GetMapping("/users/joins")
    public ResponseEntity<ApiResponse<List<PostJoinDto>>> getJoinByUser(@RequestHeader(value = "Authorization", required = false) String tokenInfo) {
        try {
            List<PostJoinDto> joins = postJoinService.getJoinByUser(tokenInfo);
            ApiResponse<List<PostJoinDto>> response = new ApiResponse<>(ResponseCode.SUCCESS_SEARCH_JOIN_POST, joins);
            return ResponseEntity.status(response.getStatus()).body(response);
        } catch (IllegalArgumentException e) {
            throw new ApiException(ErrorCode.POST_JOIN_SEARCH_USER_ERROR);
        }
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> deleteJoinByPostId(@PathVariable Long postId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String login = authentication.getName();  // 로그인된 사용자의 아이디 (login 필드)

            SiteUser user = userService.findByLogin(login);
            postJoinService.deleteJoinByPostId(user.getId(), postId);
            ApiResponse<Void> response = new ApiResponse<>(ResponseCode.SUCCESS_DELETE_JOIN_POST, null);
            return ResponseEntity.status(response.getStatus()).body(response);
        }catch (UserNotFoundException e) {
            throw new ApiException(ErrorCode.POST_JOIN_DELETE_USER_ERROR);
        } catch (PostNotFoundException e) {
            throw new ApiException(ErrorCode.POST_JOIN_DELETE_POST_ERROR); }
        catch (IllegalArgumentException e) {
            throw new ApiException(ErrorCode.POST_JOIN_DELETE_ERROR);
        }
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<Boolean>> checkUserJoinedPost(@PathVariable Long postId, @RequestHeader(value = "Authorization", required = false) String tokenInfo) {
        try {
        boolean isJoined = postJoinService.checkUserJoinPost(tokenInfo, postId);
            ApiResponse<Boolean> response = new ApiResponse<>(ResponseCode.SUCCESS_CHECK_JOIN_POST, isJoined);
            return ResponseEntity.status(response.getStatus()).body(response);
        }
        catch (PostNotFoundException e){
            throw new ApiException(ErrorCode.POST_JOIN_CHECK_POST_ERROR);
        }
        catch (IllegalArgumentException e) {
                throw new ApiException(ErrorCode.POST_JOIN_CHECK_ERROR);
            }
    }
    @GetMapping("/{postId}/join-count")
    public ResponseEntity<ApiResponse<Long>> getCountPostJoin(@PathVariable Long postId) {
        try {
        long count = postJoinService.getCountPostJoin(postId);
            ApiResponse<Long> response = new ApiResponse<>(ResponseCode.SUCCESS_COUNT_JOIN_POST, count);
            return ResponseEntity.status(response.getStatus()).body(response);
    }
        catch (PostNotFoundException e){
        throw new ApiException(ErrorCode.POST_JOIN_COUNT_POST_ERROR);
    }
        catch (IllegalArgumentException e) {
        throw new ApiException(ErrorCode.POST_JOIN_COUNT_ERROR);
    }
    }



}