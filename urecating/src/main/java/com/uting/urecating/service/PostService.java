package com.uting.urecating.service;

import com.uting.urecating.domain.Category;
import com.uting.urecating.domain.Post;
import com.uting.urecating.domain.PostJoin;
import com.uting.urecating.domain.SiteUser;
import com.uting.urecating.dto.PostSortDto;
import com.uting.urecating.dto.PostRequestDto;
import com.uting.urecating.dto.PostDetailDto;
import com.uting.urecating.jwt.TokenProvider;
import com.uting.urecating.repository.PostJoinRepository;
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
    private final PostJoinRepository postJoinRepository;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    public List<PostDetailDto> getPosts() {
//        Sort sort = Sort.by(postSortDto.sort(), postSortDto.order());
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        List<Post> postList = postRepository.findAllByStatusTrue(sort).orElseThrow(() -> new IllegalArgumentException());
        List<PostDetailDto> postDetailDtoList = postList.stream().map(p -> PostDetailDto.fromPost(p)).collect(Collectors.toList());

        return postDetailDtoList;
    }

    public PostDetailDto getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글 조회 실패"));
        PostDetailDto postDetailDto = PostDetailDto.fromPost(post);

        return postDetailDto;
    }

    public PostDetailDto createPost(String tokenInfo, PostRequestDto requestDto, MultipartFile image) throws IOException {
        String userLogin = tokenProvider.getUserLoginToken(tokenInfo);
        SiteUser user = userRepository.findByLogin(userLogin).orElseThrow(() -> new IllegalArgumentException("사용자 조회 실패"));

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

        PostJoin postJoin = new PostJoin(user, post);
        postJoinRepository.save(postJoin);
        return postDetailDto;
    }

    @Transactional
    public PostDetailDto updatePost(Long postId, String tokenInfo, PostRequestDto requestDto, MultipartFile image) throws IOException {
        String userLogin = tokenProvider.getUserLoginToken(tokenInfo);
        SiteUser user = userRepository.findByLogin(userLogin).orElseThrow(() -> new IllegalArgumentException("사용자 조회 실패"));

        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            String fileName = UUID.randomUUID().toString() + image.getOriginalFilename();
            imageUrl = s3Service.upload(image, fileName);
        }else{
            imageUrl = null;
        }

        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            throw new IllegalArgumentException("게시글 조회 실패");
        }

        if (imageUrl != null && post.getImage() != null) {
//            System.out.println("Deleting image from S3: " + user.getImage().substring(user.getImage().lastIndexOf("/")+1));
            s3Service.delete( post.getImage().substring(post.getImage().lastIndexOf("/")+1));
        }

        post.update(requestDto, imageUrl);
        postRepository.save(post);
        PostDetailDto postDetailDto = PostDetailDto.fromPost(post);

        return postDetailDto;
    }

    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글 조회 실패"));
        postRepository.deleteById(postId);
    }

    public List<PostDetailDto> getPostsByCategory(Category category) {
//        Sort sort = Sort.by(postSortDto.sort(), postSortDto.order());

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        List<Post> postList = postRepository.findAllByCategoryAndStatusTrue(category, sort).orElseThrow(() -> new IllegalArgumentException("게시글 조회 실패"));
        List<PostDetailDto> postDetailDtoList = postList.stream().map(p -> PostDetailDto.fromPost(p)).collect(Collectors.toList());

        return postDetailDtoList;
    }

    public List<PostDetailDto> getPostsByUser(String tokenInfo) {
        String userLogin = tokenProvider.getUserLoginToken(tokenInfo);
        SiteUser user = userRepository.findByLogin(userLogin).orElseThrow(() -> new IllegalArgumentException("사용자 조회 실패"));

//        Sort sort = Sort.by(postSortDto.sort(), postSortDto.order());
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

        List<Post> postList = postRepository.findAllByUserId(user.getId(), sort).orElseThrow(() -> new IllegalArgumentException("게시글 조회 실패"));
        List<PostDetailDto> postDetailDtoList = postList.stream().map(p -> PostDetailDto.fromPost(p)).collect(Collectors.toList());

        return postDetailDtoList;
    }

}
