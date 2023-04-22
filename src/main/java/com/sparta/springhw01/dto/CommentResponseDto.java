package com.sparta.springhw01.dto;

import com.sparta.springhw01.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String contents;
    private String username;
    private Long postId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    // Entity를 Dto로 변환
    public CommentResponseDto(Comment comment){
        this.id = comment.getId();
        this.contents = comment.getContents();
        this.username = comment.getUser().getUsername();
        this.postId = comment.getPost().getId();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}