package com.uting.urecating.service;


import com.uting.urecating.domain.Post;
import com.uting.urecating.domain.SiteUser;
import com.uting.urecating.domain.PostJoin;
import com.uting.urecating.dto.PostJoinDto;
import com.uting.urecating.config.exception.PostNotFoundException;
import com.uting.urecating.config.exception.UserNotFoundException;
import com.uting.urecating.jwt.TokenProvider;
import com.uting.urecating.repository.PostRepository;
import com.uting.urecating.repository.PostJoinRepository;
import com.uting.urecating.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostJoinService {

    private final PostJoinRepository postJoinRepository;
    private final TokenProvider tokenProvider;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional

    public void postJoin(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("No post found with id: " + postId));

        SiteUser user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("No user found with id: " + userId));

        if (post.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("작성자는 자신의 게시물에 참여할 수 없습니다.");
        }

        if (postJoinRepository.existsByUserAndPost(user, post)) {
            throw new IllegalArgumentException("이미 참가를 했습니다.");
        }

        long postJoinCount = postJoinRepository.countByPost(post);

        if (postJoinCount + 1 >= post.getMaxCapacity()) {
            throw new IllegalArgumentException("마감되었습니다.");
        }

        PostJoin postJoin = new PostJoin(user, post);
        postJoinRepository.save(postJoin);

        // Update the post's status if the max number of participants is reached
        if (postJoinCount + 1 >= post.getMaxCapacity()) {
            post.setStatus(false);
            postRepository.save(post);
        }
    }

    @Transactional(readOnly = true)
    public List<PostJoinDto> getJoinByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));
        List<PostJoin> postJoins = postJoinRepository.findByPost(post);
        return postJoins.stream()
                .map(postJoin -> new PostJoinDto(postJoin.getId(), postJoin.getUser().getId(), postJoin.getPost().getId()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostJoinDto> getJoinByUser(String tokenInfo) {
        String userLogin = tokenProvider.getUserLoginToken(tokenInfo);
        SiteUser user = userRepository.findByLogin(userLogin).orElseThrow(()  -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        List<PostJoin> postJoins = postJoinRepository.findByUser(user);
        return postJoins.stream()
                .map(postJoin -> new PostJoinDto(postJoin.getId(), postJoin.getUser().getId(), postJoin.getPost().getId()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteJoinByPostId(Long userId, Long postId) {
        SiteUser user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("No user found with id: " + userId));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("No post found with id: " + postId));

        PostJoin postJoin = postJoinRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new IllegalArgumentException("No join found for userId: " + userId + " and postId: " + postId));

        postJoinRepository.delete(postJoin);

        long participantCount = postJoinRepository.countByPost(post);
        if (participantCount < post.getMaxCapacity()) {
            post.setStatus(true);
            postRepository.save(post);
        }
    }

    @Transactional(readOnly = true)
    public SiteUser getUserFromToken(String tokenInfo) {
        String userLogin = tokenProvider.getUserLoginToken(tokenInfo);
        return userRepository.findByLogin(userLogin)
                .orElseThrow(() -> new UserNotFoundException("No user found with login: " + userLogin));
    }

    @Transactional(readOnly = true)
    public boolean checkUserJoinPost(String tokenInfo, Long postId) {
        SiteUser user = getUserFromToken(tokenInfo);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("No post found with id: " + postId));

        return postJoinRepository.existsByUserAndPost(user, post);
    }

    @Transactional(readOnly = true)
    public long getCountPostJoin(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("No post found with id: " + postId));
        return postJoinRepository.countByPost(post);
    }

}
