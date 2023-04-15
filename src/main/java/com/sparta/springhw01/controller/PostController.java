package com.sparta.springhw01.controller;

import com.sparta.springhw01.dto.PostRequestDto;
import com.sparta.springhw01.entity.Post;
import com.sparta.springhw01.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/")
    public ModelAndView home() {
        return new ModelAndView("index");
    }

    /* 게시글 작성 api */
    @PostMapping("/api/posts")
    public Post createPost(@RequestBody PostRequestDto requestDto){
        return postService.createPost(requestDto);
    }

    /* 게시글 전체 목록 조회 api */
    @GetMapping("/api/posts")
    public List<Post> getPosts(){
        return postService.getPosts();
    }

    /* 게시글 상세 조회 api */
    @GetMapping("/api/posts/{id}")
    public Post getPost(@PathVariable Long id){
        return postService.getPost(id);
    }

    /* 게시글 수정 api */
    @PutMapping("/api/posts/{id}")
    public Long updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto){
        return postService.update(id, requestDto);
    }

    /* 게시글 삭제 api */
    @DeleteMapping("/api/posts/{id}")
    public Long deletePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto){
        return postService.deletePost(id, requestDto);
    }
}