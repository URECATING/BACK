package com.uting.urecating.repository;

import com.uting.urecating.domain.Post;
import com.uting.urecating.domain.PostJoin;
import com.uting.urecating.domain.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostJoinRepository extends JpaRepository<PostJoin, Long>{
    List<PostJoin> findByPost(Post post);
    List<PostJoin> findByUser(SiteUser user);
    long countByPost(Post post);

    boolean existsByUserAndPost(SiteUser user, Post post);

    Optional<PostJoin> findByUserAndPost(SiteUser user, Post post);
}
