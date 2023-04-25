package com.sparta.springhw01.controller;

import com.sparta.springhw01.dto.CommentRequestDto;
import com.sparta.springhw01.dto.CommentResponseDto;
import com.sparta.springhw01.dto.StatusResponseDto;
import com.sparta.springhw01.security.UserDetailsImpl;
import com.sparta.springhw01.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    /* 댓글 작성 api */
    @PostMapping("/api/comments")
    public CommentResponseDto createComment(@RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.createComment(requestDto, userDetails.getUser());
    }

    /* 댓글 수정 api */
    @PutMapping("/api/comments/{id}")
    public CommentResponseDto updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.updateComment(id, requestDto, userDetails.getUser());
    }

    /* 댓글 삭제 api */
    @DeleteMapping("/api/comments/{id}")
    public ResponseEntity<StatusResponseDto> deleteComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        commentService.deleteComment(id, userDetails.getUser());
        StatusResponseDto res = new StatusResponseDto(
                200,
                HttpStatus.OK,
                "댓글 삭제 성공",
                null
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }
}
