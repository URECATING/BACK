package com.uting.urecating.service;

import com.uting.urecating.config.exception.PostNotFoundException;
import com.uting.urecating.config.exception.UserNotFoundException;
import com.uting.urecating.domain.LikePost;
import com.uting.urecating.domain.Post;
import com.uting.urecating.domain.SiteUser;
import com.uting.urecating.dto.LikePostDto;
import com.uting.urecating.jwt.TokenProvider;
import com.uting.urecating.repository.LikePostRepository;
import com.uting.urecating.repository.PostRepository;
import com.uting.urecating.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikePostService {
    private final LikePostRepository likePostRepository;
    private final TokenProvider tokenProvider;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addLikePost(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("No post found with id: " + postId));
        SiteUser user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("No post found with id: " + postId));
        if (likePostRepository.existsByUserAndPost(user, post)) {
            throw new IllegalArgumentException("User has already joined this post.");
        }
        LikePost likePost = new LikePost(user, post);
        likePostRepository.save(likePost);
    }

    @Transactional
    public void removeLikePost(Long userId, Long postId) {
        SiteUser user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found for userId: " + userId));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found for postId: " + postId));

        LikePost likePost = likePostRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new IllegalArgumentException("LikePost not found for userId: " + userId + " and postId: " + postId));

        likePostRepository.delete(likePost);
    }


    @Transactional(readOnly = true)
    public boolean isPostLikedByUser(Long userId, Long postId) {
        SiteUser user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("No user found with id: " + userId));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("No post found with id: " + postId));

        return likePostRepository.existsByUserAndPost(user, post);
    }

    @Transactional(readOnly = true)
    public List<LikePostDto> getLikesByUser(String tokenInfo) {
        String userLogin = tokenProvider.getUserLoginToken(tokenInfo);
        SiteUser user = userRepository.findByLogin(userLogin).orElseThrow(()  -> new UserNotFoundException("No user found with id"));

        return likePostRepository.findByUser(user).stream()
                .map(likePost -> new LikePostDto(likePost.getId(), likePost.getUser().getId(), likePost.getPost().getId()))
                .collect(Collectors.toList());
    }


}
