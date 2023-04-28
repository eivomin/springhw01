package com.sparta.springhw01.controller;

import com.sparta.springhw01.dto.CommentRequestDto;
import com.sparta.springhw01.dto.CommentResponseDto;
import com.sparta.springhw01.dto.StatusResponseDto;
import com.sparta.springhw01.exception.ApiException;
import com.sparta.springhw01.exception.ExceptionEnum;
import com.sparta.springhw01.security.UserDetailsImpl;
import com.sparta.springhw01.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
                "댓글 삭제 성공"
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }

    /* 댓글 좋아요 api */
    @PostMapping("/api/comments/{id}/like")
    public ResponseEntity<StatusResponseDto> likeComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        boolean likeCheck = commentService.saveLikes(id, userDetails.getUser());

        if(likeCheck) {
            StatusResponseDto res = new StatusResponseDto(
                    200,
                    HttpStatus.OK,
                    "댓글 좋아요 성공"
            );
            return new ResponseEntity<>(res, res.getHttpStatus());
        }else throw new ApiException(ExceptionEnum.ALREADY_LIKE_EXCEPTION);
    }

    /* 댓글 싫어요 api */
    @DeleteMapping("/api/comments/{id}/like")
    public ResponseEntity<StatusResponseDto> dislikeComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        boolean dislikeCheck = commentService.deleteLikes(id, userDetails.getUser());

        if(dislikeCheck) {
            StatusResponseDto res = new StatusResponseDto(
                    200,
                    HttpStatus.OK,
                    "댓글 싫어요 성공"
            );
            return new ResponseEntity<>(res, res.getHttpStatus());
        }else throw new ApiException(ExceptionEnum.NOT_YET_LIKE_EXCEPTION);
    }
}
