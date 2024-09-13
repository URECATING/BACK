package com.uting.urecating.repository;

import com.uting.urecating.domain.Category;
import com.uting.urecating.domain.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByUserId(Long userId);

    Optional<Post> findById(Long Id);

    List<Post> findAllByUserId(Long userId, Sort sort);
    List<Post> findAllByCategory(Category category, Sort sort);
}