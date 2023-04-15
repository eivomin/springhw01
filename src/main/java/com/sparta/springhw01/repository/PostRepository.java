package com.sparta.springhw01.repository;

import com.sparta.springhw01.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByModifiedAtDesc();
    Optional<Post> findByIdAndPassword(Long id, String password);
}
