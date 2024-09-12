package com.uting.urecating.controller;

import com.uting.urecating.domain.Category;
import com.uting.urecating.dto.PostSortDto;
import com.uting.urecating.dto.PostRequestDto;
import com.uting.urecating.dto.PostDetailDto;
import com.uting.urecating.dto.PostUpdateDto;
import com.uting.urecating.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/create")
    public PostDetailDto createPost(@RequestHeader(value = "Authorization", required = false) String tokenInfo, @RequestBody PostRequestDto requestDto){
        return postService.createPost(tokenInfo, requestDto);
    }

    @PutMapping("/{postId}")
    public PostDetailDto updatePost(@PathVariable Long postId, @RequestHeader(value = "Authorization", required = false) String tokenInfo,@RequestBody PostUpdateDto requestDto){
        return postService.updatePost(postId, tokenInfo, requestDto);
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
