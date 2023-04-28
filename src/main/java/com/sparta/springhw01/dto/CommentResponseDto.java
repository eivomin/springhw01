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
    private Long likeCount;

    public CommentResponseDto(Comment comment){
        this.id = comment.getId();
        this.contents = comment.getContents();
        this.username = comment.getUser().getUsername();
        this.postId = comment.getPost().getId();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.likeCount = comment.getLikeCount();
    }
}