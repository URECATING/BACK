package com.uting.urecating.service;


import com.uting.urecating.domain.Comment;
import com.uting.urecating.domain.Post;
import com.uting.urecating.domain.SiteUser;
import com.uting.urecating.dto.CommentDto;
import com.uting.urecating.dto.CommentFieldDto;
import com.uting.urecating.repository.CommentRepository;
import com.uting.urecating.repository.PostRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;


    public List<CommentDto> getComments(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException());

        return commentRepository.findByPost(postId)
                .stream()
                .map(comment -> {
                    SiteUser user = comment.getUser();
                    return CommentDto.createCommentDto(comment, user, post);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentDto createComments(Long postId, CommentFieldDto dto, SiteUser user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException());
        Comment comment = Comment.builder()
                .content(dto.getContent())
                .post(post)
                .user(user)
                .build();

        Comment created = commentRepository.save(comment);
        return CommentDto.createCommentDto(created, user, post);
    }

    @Transactional
    public CommentDto update(Long id, CommentFieldDto dto) {
        Comment target = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());
        Comment updatedComment = Comment.builder()
                .id(target.getId()) // 기존 ID 유지
                .content(dto.getContent() != null ? dto.getContent() : target.getContent()) // 새로운 content 또는 기존 content
                .post(target.getPost()) // 기존 post 유지
                .user(target.getUser()) // 기존 user 유지
                .parent(target.getParent()) // 부모 댓글 유지 (대댓글)
                .children(target.getChildren()) // 자식 댓글 유지
                .build();

        Comment savedComment = commentRepository.save(updatedComment);
        return CommentDto.createCommentDto(savedComment, savedComment.getUser(), savedComment.getPost());
    }

    @Transactional
    public CommentDto delete(Long id) {
        Comment target = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());
        commentRepository.delete(target);
        return CommentDto.createCommentDto(target, target.getUser(), target.getPost());}
}

































