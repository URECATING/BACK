package com.uting.urecating.service;

import com.uting.urecating.domain.Category;
import com.uting.urecating.domain.Post;
import com.uting.urecating.domain.SiteUser;
import com.uting.urecating.dto.PostSortDto;
import com.uting.urecating.dto.PostRequestDto;
import com.uting.urecating.dto.PostDetailDto;
import com.uting.urecating.jwt.TokenProvider;
import com.uting.urecating.repository.PostRepository;
import com.uting.urecating.repository.UserRepository;
import com.uting.urecating.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    public List<PostDetailDto> getPosts(PostSortDto postSortDto) {
        Sort sort = Sort.by(postSortDto.sort(), postSortDto.order());
        List<Post> postList = postRepository.findAll(sort);
        List<PostDetailDto> postDetailDtoList = postList.stream().map(p -> PostDetailDto.fromPost(p)).collect(Collectors.toList());

        return postDetailDtoList;
    }

    public PostDetailDto getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post not found"));
        PostDetailDto postDetailDto = PostDetailDto.fromPost(post);

        return postDetailDto;
    }

    public PostDetailDto createPost(String tokenInfo, PostRequestDto requestDto, MultipartFile image) throws IOException {
        String userLogin = tokenProvider.getUserLoginToken(tokenInfo);
        SiteUser user = userRepository.findByLogin(userLogin).orElse(null);

        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            String fileName = UUID.randomUUID().toString() + image.getOriginalFilename();
            imageUrl = s3Service.upload(image, fileName);
        }else{
            imageUrl = null;
        }

        Post post = new Post(
                user,
                requestDto.title(),
                requestDto.content(),
                imageUrl,
                requestDto.category(),
                requestDto.meetingDate(),
                requestDto.place(),
                requestDto.maxCapacity(),
                true);

        postRepository.save(post);
        PostDetailDto postDetailDto = PostDetailDto.fromPost(post);
        return postDetailDto;
    }

    @Transactional
    public PostDetailDto updatePost(Long postId, String tokenInfo, PostRequestDto requestDto, MultipartFile image) throws IOException {
        String userLogin = tokenProvider.getUserLoginToken(tokenInfo);
        SiteUser user = userRepository.findByLogin(userLogin).orElse(null);

        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            String fileName = UUID.randomUUID().toString() + image.getOriginalFilename();
            imageUrl = s3Service.upload(image, fileName);
        }else{
            imageUrl = null;
        }

        Post post = postRepository.findById(postId).orElse(null);
        post.update(requestDto, imageUrl);
        postRepository.save(post);
        PostDetailDto postDetailDto = PostDetailDto.fromPost(post);

        return postDetailDto;
    }

    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    public List<PostDetailDto> getPostsByCategory(Category category, PostSortDto postSortDto) {
        Sort sort = Sort.by(postSortDto.sort(), postSortDto.order());
        List<Post> postList = postRepository.findAllByCategory(category, sort);
        List<PostDetailDto> postDetailDtoList = postList.stream().map(p -> PostDetailDto.fromPost(p)).collect(Collectors.toList());

        return postDetailDtoList;
    }

    public List<PostDetailDto> getPostsByUser(String tokenInfo, PostSortDto postSortDto) {
        String userLogin = tokenProvider.getUserLoginToken(tokenInfo);
        SiteUser user = userRepository.findByLogin(userLogin).orElse(null);

        Sort sort = Sort.by(postSortDto.sort(), postSortDto.order());
        List<Post> postList = postRepository.findAllByUserId(user.getId(), sort);
        List<PostDetailDto> postDetailDtoList = postList.stream().map(p -> PostDetailDto.fromPost(p)).collect(Collectors.toList());

        return postDetailDtoList;
    }

}
