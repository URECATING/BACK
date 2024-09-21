package com.uting.urecating.service;

import com.uting.urecating.config.exception.PostNotFoundException;
import com.uting.urecating.config.exception.UserNotFoundException;
import com.uting.urecating.domain.LikePost;
import com.uting.urecating.domain.Post;
import com.uting.urecating.domain.SiteUser;
import com.uting.urecating.repository.LikePostRepository;
import com.uting.urecating.repository.PostRepository;
import com.uting.urecating.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikePostService {
    private final LikePostRepository likePostRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addLikePost(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("No post found with id: " + postId));
        SiteUser user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("No post found with id: " + postId));

        LikePost likePost = new LikePost(user, post);
        likePostRepository.save(likePost);
    }

    @Transactional
    public void removeLikePost(Long userId, Long postId) {
        SiteUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found for userId: " + userId));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found for postId: " + postId));

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
    public List<LikePost> getLikesByUser(Long userId) {
        SiteUser user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("No user found with id: " + userId));

        return likePostRepository.findByUser(user);
    }


}
