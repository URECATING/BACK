package com.uting.urecating.repository;

import com.uting.urecating.domain.Category;
import com.uting.urecating.domain.Post;
import jakarta.persistence.LockModeType;
import org.hibernate.LockMode;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<List<Post>> findAllByUserId(Long userId);

    Optional<Post> findById(Long Id);

    Optional<List<Post>> findAllByUserId(Long userId, Sort sort);
    Optional<List<Post>> findAllByStatusTrue(Sort sort);
    Optional<List<Post>> findAllByCategoryAndStatusTrue(Category category, Sort sort);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Post p WHERE p.id = :postId")
    Optional<Post> findByIdWithLock(Long postId);
}