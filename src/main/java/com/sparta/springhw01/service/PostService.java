package com.sparta.springhw01.service;

import com.sparta.springhw01.dto.PostRequestDto;
import com.sparta.springhw01.entity.Post;
import com.sparta.springhw01.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Post createPost(PostRequestDto requestDto) {
        Post post = new Post(requestDto);
        postRepository.save(post);
        return post;
    }

    @Transactional(readOnly = true)
    public List<Post> getPosts() {
        return postRepository.findAllByOrderByModifiedAtDesc();
    }

    @Transactional
    public Post update(Long id, PostRequestDto requestDto) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글입니다.")
        );

        postRepository.findByIdAndPassword(id, requestDto.getPassword()).orElseThrow(
                () -> new IllegalArgumentException("패스워드가 일치하지 않습니다.")
        );

        post.update(requestDto);
        return post;
    }

    @Transactional
    public boolean deletePost(Long id, PostRequestDto requestDto) {
        postRepository.findByIdAndPassword(id, requestDto.getPassword()).orElseThrow(
                () -> new IllegalArgumentException("패스워드가 일치하지 않습니다.")
        );

        postRepository.deleteById(id);
        return true;
    }

    @Transactional(readOnly = true)
    public Post getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () ->  new IllegalArgumentException("존재하지 않는 게시글입니다.")
        );
        return post;
    }
}
