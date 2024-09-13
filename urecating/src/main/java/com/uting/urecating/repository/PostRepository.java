package com.uting.urecating.repository;

import com.uting.urecating.domain.Category;
import com.uting.urecating.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByUserId(Long userId, Sort sort);

    List<Post> findAllByCategory(Category category, Sort sort);
}
