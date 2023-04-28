package com.sparta.springhw01.controller;

import com.sparta.springhw01.dto.PostRequestDto;
import com.sparta.springhw01.dto.PostResponseDto;
import com.sparta.springhw01.dto.StatusResponseDto;
import com.sparta.springhw01.exception.ApiException;
import com.sparta.springhw01.exception.ExceptionEnum;
import com.sparta.springhw01.security.UserDetailsImpl;
import com.sparta.springhw01.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.createPost(requestDto, userDetails.getUser());
    }

    /* 게시글 전체 목록 조회 api */
    @GetMapping("/api/posts")
    public List<PostResponseDto> getPosts(){
        return postService.getPosts();
    }

    /* 게시글 상세 조회 api */
    @GetMapping("/api/posts/{id}")
    public PostResponseDto getPost(@PathVariable Long id){
        return postService.getPost(id);
    }

    /* 게시글 수정 api */
    @PutMapping("/api/posts/{id}")
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.update(id, requestDto, userDetails.getUser());
    }

    /* 게시글 삭제 api */
    @DeleteMapping("/api/posts/{id}")
    public ResponseEntity<StatusResponseDto> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        postService.deletePost(id, userDetails.getUser());

        StatusResponseDto res = new StatusResponseDto(
                200,
                HttpStatus.OK,
                "게시글 삭제 성공"
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }

    /* 게시글 좋아요 api */
    @PostMapping("/api/posts/{id}/like")
    public ResponseEntity<StatusResponseDto> likePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        boolean likeCheck = postService.saveLikes(id, userDetails.getUser());

        if(likeCheck) {
            StatusResponseDto res = new StatusResponseDto(
                    200,
                    HttpStatus.OK,
                    "게시글 좋아요 성공"
            );
            return new ResponseEntity<>(res, res.getHttpStatus());
        }else throw new ApiException(ExceptionEnum.ALREADY_LIKE_EXCEPTION);
    }

    /* 게시글 싫어요 api */
    @DeleteMapping("/api/posts/{id}/like")
    public ResponseEntity<StatusResponseDto> dislikePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        boolean dislikeCheck = postService.deleteLikes(id, userDetails.getUser());

        if(dislikeCheck) {
            StatusResponseDto res = new StatusResponseDto(
                    200,
                    HttpStatus.OK,
                    "게시글 싫어요 성공"
            );
            return new ResponseEntity<>(res, res.getHttpStatus());
        }else throw new ApiException(ExceptionEnum.NOT_YET_LIKE_EXCEPTION);
    }

}
