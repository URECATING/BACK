package com.uting.urecating.repository;

import com.uting.urecating.domain.LikePost;
import com.uting.urecating.domain.Post;
import com.uting.urecating.domain.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikePostRepository extends JpaRepository<LikePost, Long> {

    // 유저가 게시물에 좋아요를 했는지 확인
    boolean existsByUserAndPost(SiteUser user, Post post);

    // 게시물의 좋아요 개수 확인
    int countByPost(Post post);

    // 좋아요 목록을 조회
    List<LikePost> findByUser(SiteUser user);

}
