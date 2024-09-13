package com.uting.urecating.controller;

import com.uting.urecating.domain.Category;
import com.uting.urecating.dto.PostSortDto;
import com.uting.urecating.dto.PostRequestDto;
import com.uting.urecating.dto.PostDetailDto;
import com.uting.urecating.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
    public List<PostDetailDto> getPosts(@ModelAttribute PostSortDto postSortDto) {
        return postService.getPosts(postSortDto);
    }

    @GetMapping("/{postId}")
    public PostDetailDto getPost(@PathVariable Long postId){
        return postService.getPost(postId);
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PostDetailDto createPost(@RequestHeader(value = "Authorization", required = false) String tokenInfo, @RequestPart PostRequestDto requestDto, @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        return postService.createPost(tokenInfo, requestDto, image);
    }

    @PutMapping("/{postId}")
    public PostDetailDto updatePost(@PathVariable Long postId, @RequestHeader(value = "Authorization", required = false) String tokenInfo,@RequestPart PostRequestDto requestDto, @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        return postService.updatePost(postId, tokenInfo, requestDto, image);
    }

    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable Long postId){
        postService.deletePost(postId);
    }

    @GetMapping("/category/{category}")
    public List<PostDetailDto> getPostsByCategory(@PathVariable String category, @ModelAttribute PostSortDto postSortDto){
        return postService.getPostsByCategory(Category.valueOf(category), postSortDto);
    }

    @GetMapping("/userPost")
    public List<PostDetailDto> getPostsByUserId(@RequestHeader(value = "Authorization", required = false) String tokenInfo, @ModelAttribute PostSortDto postSortDto){
        return postService.getPostsByUser(tokenInfo, postSortDto);
    }
}
