package com.uting.urecating.service;


import com.uting.urecating.domain.Post;
import com.uting.urecating.domain.SiteUser;
import com.uting.urecating.domain.PostJoin;
import com.uting.urecating.dto.PostJoinDto;
import com.uting.urecating.config.exception.PostNotFoundException;
import com.uting.urecating.config.exception.UserNotFoundException;
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
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional

    public void postJoin(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("No post found with id: " + postId));

        SiteUser user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("No user found with id: " + userId));

        if (postJoinRepository.existsByUserAndPost(user, post)) {
            throw new IllegalArgumentException("User has already joined this post.");
        }
        PostJoin postJoin = new PostJoin(user, post);
        postJoinRepository.save(postJoin);
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
    public List<PostJoinDto> getJoinByUser(Long userId) {
        SiteUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        List<PostJoin> postJoins = postJoinRepository.findByUser(user);
        return postJoins.stream()
                .map(postJoin -> new PostJoinDto(postJoin.getId(), postJoin.getUser().getId(), postJoin.getPost().getId()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteJoinById(Long joinId) {
        PostJoin postJoin = postJoinRepository.findById(joinId)
                .orElseThrow(() -> new IllegalArgumentException("참가 정보를 찾을 수 없습니다."));
        postJoinRepository.delete(postJoin);
    }




}
