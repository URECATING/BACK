package com.uting.urecating.controller;

import com.uting.urecating.config.exception.ApiException;
import com.uting.urecating.config.exception.ErrorCode;
import com.uting.urecating.config.response.ApiResponse;
import com.uting.urecating.config.response.ResponseCode;
import com.uting.urecating.domain.Category;
import com.uting.urecating.domain.Post;
import com.uting.urecating.dto.PostSortDto;
import com.uting.urecating.dto.PostRequestDto;
import com.uting.urecating.dto.PostDetailDto;
import com.uting.urecating.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PostDetailDto>>> getPosts() {
        try {
            List<PostDetailDto> posts = postService.getPosts();
            ApiResponse<List<PostDetailDto>> response = new ApiResponse<>(ResponseCode.SUCCESS_SEARCH_POST, posts);
            return ResponseEntity.status(response.getStatus()).body(response);
        }catch (IllegalArgumentException e){
            throw new ApiException(ErrorCode.POST_SEARCH_ERROR);
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostDetailDto>> getPost(@PathVariable Long postId){
        try {
            PostDetailDto post = postService.getPost(postId);
            ApiResponse<PostDetailDto> response = new ApiResponse<>(ResponseCode.SUCCESS_SEARCH_POST, post);
            return ResponseEntity.status(response.getStatus()).body(response);
        }catch (IllegalArgumentException e){
            throw new ApiException(ErrorCode.POST_SEARCH_ERROR);
        }
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PostDetailDto>> createPost(@RequestHeader(value = "Authorization", required = false) String tokenInfo, PostRequestDto requestDto, @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        try {
            PostDetailDto post = postService.createPost(tokenInfo, requestDto, image);
            ApiResponse<PostDetailDto> response = new ApiResponse<>(ResponseCode.SUCCESS_CREATE_POST, post);
            return ResponseEntity.status(response.getStatus()).body(response);
        } catch (IllegalArgumentException e) {
            throw new ApiException(ErrorCode.POST_CREATE_ERROR);
        }
    }

    @PutMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PostDetailDto>> updatePost(@PathVariable Long postId, @RequestHeader(value = "Authorization", required = false) String tokenInfo, PostRequestDto requestDto, @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        try {
            PostDetailDto post = postService.updatePost(postId, tokenInfo, requestDto, image);
            ApiResponse<PostDetailDto> response = new ApiResponse<>(ResponseCode.SUCCESS_UPDATE_POST, post);
            return ResponseEntity.status(response.getStatus()).body(response);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("게시글 조회 실패")){
                throw new ApiException(ErrorCode.POST_UPDATE_POST_ERROR);
            }else{
                throw new ApiException(ErrorCode.POST_UPDATE_USER_ERROR);
            }
        }
    }

    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable Long postId){
        postService.deletePost(postId);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<PostDetailDto>>> getPostsByCategory(@PathVariable String category){
        try {
            List<PostDetailDto> posts = postService.getPostsByCategory(Category.valueOf(category));
            ApiResponse<List<PostDetailDto>> response = new ApiResponse<>(ResponseCode.SUCCESS_CATEGORY_POST, posts);
            return ResponseEntity.status(response.getStatus()).body(response);
        }catch (IllegalArgumentException e){
            throw new ApiException(ErrorCode.POST_CATEGORY_ERROR);
        }
    }

    @GetMapping("/userPost")
    public ResponseEntity<ApiResponse<List<PostDetailDto>>> getPostsByUserId(@RequestHeader(value = "Authorization", required = false) String tokenInfo){
        try {
            List<PostDetailDto> posts = postService.getPostsByUser(tokenInfo);
            ApiResponse<List<PostDetailDto>> response = new ApiResponse<>(ResponseCode.SUCCESS_USER_POST, posts);
            return ResponseEntity.status(response.getStatus()).body(response);
        }catch (IllegalArgumentException e){
            if (e.getMessage().equals("게시글 조회 실패")){
                throw new ApiException(ErrorCode.POST_USER_POST_ERROR);
            }else{
                throw new ApiException(ErrorCode.POST_USER_USER_ERROR);
            }
        }
    }
}
