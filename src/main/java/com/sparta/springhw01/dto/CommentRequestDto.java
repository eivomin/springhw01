package com.sparta.springhw01.dto;

import com.sparta.springhw01.entity.Post;
import com.sparta.springhw01.entity.User;
import lombok.Getter;

@Getter
public class CommentRequestDto {
    private Long postId;
    private String contents;
}