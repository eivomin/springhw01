package com.sparta.springhw01.controller;

import com.google.gson.JsonObject;
import com.sparta.springhw01.dto.CommentRequestDto;
import com.sparta.springhw01.dto.CommentResponseDto;
import com.sparta.springhw01.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    /* 댓글 작성 api */
    @PostMapping("/api/comments")
    public CommentResponseDto createPost(@RequestBody CommentRequestDto requestDto, HttpServletRequest request){
        return commentService.createComment(requestDto, request);
    }

    /* 댓글 수정 api */
    @PutMapping("/api/comments/{id}")
    public CommentResponseDto updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request){
        return commentService.updateComment(id, requestDto, request);
    }

    /* 댓글 삭제 api */
    @DeleteMapping("/api/comments/{id}")
    public String deleteComment(@PathVariable Long id, HttpServletRequest request){
        boolean flag = commentService.deleteComment(id, request);
        JsonObject jsonObj = new JsonObject();

        if(flag){
            jsonObj.addProperty("msg", "댓글 삭제 성공");
            jsonObj.addProperty("statusCode", "200");
        }

        return jsonObj.toString();
    }
}
