package com.sparta.springhw01.controller;

import com.google.gson.JsonObject;
import com.sparta.springhw01.dto.CommentRequestDto;
import com.sparta.springhw01.dto.CommentResponseDto;
import com.sparta.springhw01.dto.StatusResponseDto;
import com.sparta.springhw01.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    /* 댓글 작성 api */
    @PostMapping("/api/comments")
    public CommentResponseDto createComment(@RequestBody CommentRequestDto requestDto, HttpServletRequest request){
        return commentService.createComment(requestDto, request);
    }

    /* 댓글 수정 api */
    @PutMapping("/api/comments/{id}")
    public CommentResponseDto updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request){
        return commentService.updateComment(id, requestDto, request);
    }

    /* 댓글 삭제 api */
    @DeleteMapping("/api/comments/{id}")
    public ResponseEntity<StatusResponseDto> deleteComment(@PathVariable Long id, HttpServletRequest request){
        commentService.deleteComment(id, request);
        StatusResponseDto res = new StatusResponseDto(
                200,
                HttpStatus.OK,
                "댓글 삭제 성공",
                null
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }
}
